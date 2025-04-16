package kr.hhplus.be.server.concurrency;

import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.usecase.UsePointUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UsePointConcurrencyTest {

    @Autowired
    PointRepository pointRepository;

    @Autowired
    UsePointUseCase usePointUseCase;

    @BeforeEach
    void setUp() {
        // 초기 포인트 10_000으로 설정
        pointRepository.save(new UserPoint(1L, 10_000L, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void 동시에_포인트를_사용하면_정합성_문제가_발생한다() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    usePointUseCase.execute(1L, 1_000L); // 각 스레드가 1000원씩 차감
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        UserPoint result = pointRepository.findById(1L);
        System.out.println("최종 잔액 = " + result.point());

        // 실패하는 테스트: 실제로는 0이 아닐 수 있음 (정합성 깨짐)
        assertThat(result.point()).isEqualTo(0L);
    }
}
