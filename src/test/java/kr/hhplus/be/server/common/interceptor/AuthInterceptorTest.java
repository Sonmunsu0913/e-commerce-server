package kr.hhplus.be.server.common.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthInterceptorTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 관리자_API_정상_접근() throws Exception {
        mockMvc.perform(get("/api/admin/test")
                .header("X-Auth-Token", "admin-token"))
            .andExpect(status().isOk())
            .andExpect(content().string("관리자 전용 응답입니다."));
    }

    @Test
    void 관리자_API_인증없으면_403() throws Exception {
        mockMvc.perform(get("/api/admin/test"))
            .andExpect(status().isForbidden());
    }

    @Test
    void 관리자_API_잘못된_토큰이면_403() throws Exception {
        mockMvc.perform(get("/api/admin/test")
                .header("X-Auth-Token", "invalid-token"))
            .andExpect(status().isForbidden());
    }
}
