package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.coupon.Coupon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
        // 상품 등록
        productRepository.save(new Product(1L, "화과자", 5000, 10));

        // 포인트 충전
        userPointRepository.save(new UserPoint(1L, 20000, LocalDateTime.now(), LocalDateTime.now(), 0));

        // 쿠폰 등록
        couponRepository.save(new Coupon(101L, "1000원 할인 쿠폰", 1000, 5));
    }

    @Test
    void 주문_요청이_API를_통해_정상_처리된다() throws Exception {
        String json = """
            {
              "userId": 1,
              "items": [
                {
                  "productId": 1,
                  "productName": "화과자",
                  "price": 5000,
                  "quantity": 2
                }
              ],
              "couponId": 101
            }
        """;

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPrice").value(9000))
                .andExpect(jsonPath("$.pointAfterPayment").value(11000));
    }
}
