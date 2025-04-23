package kr.hhplus.be.server.concurrency;

import java.util.concurrent.atomic.AtomicInteger;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.util.StopWatch;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsePointConcurrencyTest {

    @Autowired
    UserPointRepository userPointRepository;

    @Autowired
    UsePointService usePointService;

    @BeforeEach
    void setUp() {
        // 초기 포인트 10_000으로 설정
        userPointRepository.save(new UserPoint(1L, 10_000L, LocalDateTime.now(), LocalDateTime.now(), 0));
    }

    @Test
    void 동시에_포인트를_사용하면_정합성_문제가_발생한다() throws InterruptedException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("포인트 동시 사용 발급 테스트");

        System.out.println("\n[TEST] 포인트 동시 사용 테스트 시작 ===================");

        // given
        long userId = 1L;
        long initialPoint = 10_000L;
        long useAmount = 1_000L;

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    usePointService.execute(userId, useAmount);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    System.out.println(Thread.currentThread().getName() + " 차감 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // when
        UserPoint userPoint = userPointRepository.findById(userId);
        if (userPoint == null) {
            throw new IllegalStateException("해당 유저의 포인트가 존재하지 않습니다.");
        }
        long finalPoint = userPoint.point();
        long expectedPoint = initialPoint - (useAmount * successCount.get());

        // then
        System.out.println("성공: " + successCount.get());
        System.out.println("실패: " + failCount.get());
        System.out.println("최종 잔액: " + finalPoint);
        System.out.println("[TEST] 포인트 동시 사용 테스트 종료 ===================");

        assertThat(finalPoint).isEqualTo(expectedPoint);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
