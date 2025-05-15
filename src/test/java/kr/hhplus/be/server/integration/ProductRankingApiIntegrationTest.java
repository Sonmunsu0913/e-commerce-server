package kr.hhplus.be.server.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductRankingApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 인기상품_랭킹_Redis_기반_조회_성공() throws Exception {
        mockMvc.perform(get("/api/product/sale/statistics/ranking/redis")
                .param("range", "3d"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(5)))
            .andExpect(jsonPath("$[0].productId").exists())
            .andExpect(jsonPath("$[0].score").exists());
    }
}
