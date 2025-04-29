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
import org.springframework.util.StopWatch;

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

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("동시 주문 테스트");

        System.out.println("\n[TEST] 동시 주문 시 재고 초과 테스트 시작 ===================");

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<String>>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            futures.add(executor.submit(() -> {
                try {
                    System.out.println("[" + idx + "] 주문 요청 시도");
                    OrderRequest request = new OrderRequest(
                            1L,
                            List.of(new OrderItemCommand(1L, "재고 테스트 상품", 1000, 3))
                    );
                    ResponseEntity<String> resp = restTemplate.postForEntity("/api/order", request, String.class);
                    System.out.println("[" + idx + "] 응답 코드: " + resp.getStatusCode());
                    return resp;
                } catch (Exception e) {
                    System.out.println("[" + idx + "] 요청 실패: " + e.getMessage());
                    return null;
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();

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
        System.out.println("[TEST] 동시 주문 테스트 종료 ===================");

        assertThat(successCount).isLessThan(2);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
