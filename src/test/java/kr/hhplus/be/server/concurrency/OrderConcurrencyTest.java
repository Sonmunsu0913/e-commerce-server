package kr.hhplus.be.server.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import kr.hhplus.be.server.infrastructure.point.entity.UserPointEntity;
import kr.hhplus.be.server.infrastructure.point.repository.JpaUserPointRepository;
import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import kr.hhplus.be.server.infrastructure.product.repository.JpaProductRepository;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderConcurrencyTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    JpaProductRepository productRepository;

    @Autowired
    JpaUserPointRepository userPointRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        productRepository.save(new ProductEntity(1L, "재고 테스트 상품", 1000, 5)); // 재고 5개

        userPointRepository.deleteAll();
        userPointRepository.save(new UserPointEntity(1L, 10000L, LocalDateTime.now(), LocalDateTime.now()));  // 포인트 넉넉하게 충전
    }

    @Test
    void 동시에_주문하면_재고가_초과될_수_있다() throws Exception {
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<String>>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                try {
                    OrderRequest request = new OrderRequest(
                            1L,
                            List.of(new OrderItemCommand(1L, "재고 테스트 상품", 1000, 3))  // ✔️ 모든 필드 포함
                    );
                    return restTemplate.postForEntity("/api/order", request, String.class);
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();  // 모든 스레드 종료 대기

        // 응답 상태 코드 출력 (디버깅용)
//        futures.stream().forEach(f -> {
//            try {
//                var resp = f.get();
//                System.out.println("응답 코드: " + (resp != null ? resp.getStatusCode() : "null"));
//                System.out.println("응답 본문: " + (resp != null ? resp.getBody() : "null"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });

        long successCount = futures.stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(resp -> resp != null && resp.getStatusCode().is2xxSuccessful())
                .count();

        System.out.println("성공 응답 수: " + successCount);

        assertThat(successCount).isLessThan(2);  // 둘 다 성공하면 재고 초과 → 실패해야 정상
    }
}
