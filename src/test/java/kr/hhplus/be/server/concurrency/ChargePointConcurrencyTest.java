package kr.hhplus.be.server.concurrency;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ChargePointConcurrencyTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PointRepository pointRepository;

    @BeforeEach
    void setUp() {
        pointRepository.save(new UserPoint(1L, 2000L, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void 동시에_충전하면_합산_정확하지_않을_수_있다() throws Exception {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        int chargeAmount = 1000;

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    mockMvc.perform(post("/api/point/charge")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\"userId\":1, \"amount\":" + chargeAmount + "}"))
                        .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 요청 대기

        // 포인트 합산 기대값 확인
        mockMvc.perform(get("/api/point/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(2000 + (chargeAmount * threadCount))); // 실패하면 동시성 문제
    }
}
