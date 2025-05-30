package kr.hhplus.be.server.integration;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
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
class PointIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserPointRepository userPointRepository;

    @Test
    void 포인트_충전_정상_동작() throws Exception {
        userPointRepository.save(new UserPoint(1L, 0L, LocalDateTime.now(), LocalDateTime.now(), 0));

        mockMvc.perform(post("/api/point/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1, \"amount\":1000}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.amount").value(1000));
    }

    @Test
    void 포인트_조회_정상_동작() throws Exception {
        userPointRepository.save(new UserPoint(2L, 500L, LocalDateTime.now(), LocalDateTime.now(), 0));

        mockMvc.perform(get("/api/point/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(2));
    }

    @Test
    void 포인트_사용_정상_동작() throws Exception {
        userPointRepository.save(new UserPoint(3L, 2000L, LocalDateTime.now(), LocalDateTime.now(), 0));

        mockMvc.perform(post("/api/point/use")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":3, \"amount\":500}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userId").value(3))
            .andExpect(jsonPath("$.amount").value(1500));
    }

}
