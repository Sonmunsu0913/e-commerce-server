package kr.hhplus.be.server.common.filter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class LoggingFilterIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 요청시_로깅필터가_정상작동한다() throws Exception {
        mockMvc.perform(get("/api/product")) // 존재하는 아무 API
            .andReturn(); // 결과는 버려도 콘솔 로그 확인 가능
    }
}
