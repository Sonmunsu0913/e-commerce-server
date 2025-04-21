package kr.hhplus.be.server.concurrency;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.point.UserPointRepository;
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
    UserPointRepository userPointRepository;

    @BeforeEach
    void setUp() {
        userPointRepository.save(new UserPoint(1L, 2000L, LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void 동시에_충전하면_합산_정확하지_않을_수_있다() throws Exception {
        System.out.println("\n[TEST] 포인트 동시 충전 테스트 시작 ===================");

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        int chargeAmount = 1000;

        for (int i = 0; i < threadCount; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    System.out.println("[" + idx + "] 충전 시도");
                    mockMvc.perform(post("/api/point/charge")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .content("{\"userId\":1, \"amount\":" + chargeAmount + "}"))
                            .andExpect(status().isOk());
                    System.out.println("[" + idx + "] 충전 성공");
                } catch (Exception e) {
                    System.out.println("[" + idx + "] 충전 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        // 응답 받아오기
        var mvcResult = mockMvc.perform(get("/api/point/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // JSON 응답 파싱
        String json = mvcResult.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        long actual = mapper.readTree(json).get("amount").asLong();

        System.out.println("💸 실제 잔액 = " + actual);
        System.out.println("[TEST] 포인트 동시 충전 테스트 종료 ===================");
    }
}
