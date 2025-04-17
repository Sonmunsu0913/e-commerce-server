package kr.hhplus.be.server.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import kr.hhplus.be.server.infrastructure.coupon.repository.JpaCouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IssueCouponConcurrencyTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    JpaCouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        couponRepository.deleteAll();
        // 수량이 1개인 테스트 쿠폰 저장
        couponRepository.save(new CouponEntity(1L, "테스트 쿠폰", 5000, 1, 0)); // total: 1, issued: 0
    }

    @Test
    void 동시에_쿠폰을_발급하면_중복_발급될_수_있다() throws Exception {
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<String>>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            long userId = i + 1;
            futures.add(executor.submit(() -> {
                try {
                    return restTemplate.postForEntity("/api/coupon/" + userId, null, String.class);
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();

        // 응답 출력
//        futures.forEach(f -> {
//            try {
//                var resp = f.get();
//                System.out.println("응답 코드: " + resp.getStatusCode());
//                System.out.println("응답 본문: " + resp.getBody());
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
        assertThat(successCount).isLessThan(2);  // 둘 다 성공하면 동시성 문제 발생
    }
}
