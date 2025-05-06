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
import org.springframework.util.StopWatch;

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
        couponRepository.save(new CouponEntity(1L, "테스트 쿠폰", 5000, 2, 0)); // total: 2, issued: 0
    }

    @Test
    void 동시에_쿠폰을_발급하면_중복_발급될_수_있다() throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("쿠폰 동시 발급 테스트");

        System.out.println("\n[TEST] 쿠폰 동시 발급 테스트 시작 ===================");

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<String>>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final long userId = i + 1;
            final int idx = i;
            futures.add(executor.submit(() -> {
                try {
                    System.out.println("[" + idx + "] 쿠폰 발급 시도 (userId = " + userId + ")");
                    ResponseEntity<String> resp = restTemplate.postForEntity(
                        "/api/coupon/" + userId + "/coupon/1", // 🔥 couponId를 추가!
                        null,
                        String.class
                    );                    System.out.println("[" + idx + "] 응답 코드: " + resp.getStatusCode());
                    return resp;
                } catch (Exception e) {
                    System.out.println("[" + idx + "] 쿠폰 발급 실패: " + e.getMessage());
                    return null;
                } finally {
                    latch.countDown();
                }
            }));
        }

        latch.await();

        long successCount = 0;

        for (int i = 0; i < futures.size(); i++) {
            try {
                ResponseEntity<String> resp = futures.get(i).get();
                if (resp != null && resp.getStatusCode().is2xxSuccessful()) {
                    System.out.println("[UserId = " + (i + 1) + "] 쿠폰 발급 성공");
                    successCount++;
                } else {
                    System.out.println("[UserId = " + (i + 1) + "] 쿠폰 발급 실패 - 응답 코드: " + (resp != null ? resp.getStatusCode() : "null"));
                }
            } catch (Exception e) {
                System.out.println("[UserId = " + (i + 1) + "] 쿠폰 발급 실패 - 예외 발생: " + e.getMessage());
            }
        }
        System.out.println("총 성공 응답 수: " + successCount);
        System.out.println("[TEST] 쿠폰 동시 발급 테스트 종료 ===================");

        assertThat(successCount).isEqualTo(2);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
