package kr.hhplus.be.server.common.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductAuthInterceptorTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void 관리자_권한이_있으면_상품등록_성공한다() throws Exception {
        mockMvc.perform(post("/api/product/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Role", "ADMIN")
                .content("""
                    {
                      "name": "테스트 상품",
                      "price": 10000,
                      "stock": 50
                    }
                """))
            .andExpect(status().isOk())
            .andExpect(content().string("상품 등록 성공!"));
    }

    @Test
    void 관리자_권한이_없으면_403_응답한다() throws Exception {
        mockMvc.perform(post("/api/product/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "테스트 상품",
                      "price": 10000,
                      "stock": 50
                    }
                """))
            .andExpect(status().isForbidden());
    }

    @Test
    void 잘못된_권한이면_403_응답한다() throws Exception {
        mockMvc.perform(post("/api/product/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-User-Role", "USER")
                .content("""
                    {
                      "name": "테스트 상품",
                      "price": 10000,
                      "stock": 50
                    }
                """))
            .andExpect(status().isForbidden());
    }
}
