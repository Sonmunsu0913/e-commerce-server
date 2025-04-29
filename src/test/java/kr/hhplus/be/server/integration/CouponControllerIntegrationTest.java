package kr.hhplus.be.server.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CouponControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CouponRepository couponRepository;
    @Autowired private UserCouponRepository userCouponRepository;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setup() {
        // 중복 save 제거
        if (!couponRepository.existsById(1L)) {
            couponRepository.save(new Coupon(1L, "1000원 할인 쿠폰", 1000, 3));
        }
    }

    @Test
    void 쿠폰을_정상적으로_발급받는다() throws Exception {

        // when + then
        mockMvc.perform(post("/api/coupon/1")) // userId = 1
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.couponId").value(1))
            .andExpect(jsonPath("$.discountAmount").value(1000));

        List<UserCoupon> issued = userCouponRepository.findAllByUserId(1L);
        assertThat(issued).hasSize(1);
        assertThat(issued.get(0).couponId()).isEqualTo(1L);
    }

    @Test
    void 쿠폰_중복_발급_불가() throws Exception {
        couponRepository.save(new Coupon(2L, "1000원 할인 쿠폰", 1000, 3));

        // 첫 발급 - OK
        mockMvc.perform(post("/api/coupon/1"))
                .andExpect(status().isOk());

        // 중복 발급 - 예외 → GlobalExceptionHandler에서 400으로 매핑해야 통과됨
        mockMvc.perform(post("/api/coupon/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("이미 발급받은 미사용 쿠폰이 있습니다."));
    }

    @Test
    void 보유한_쿠폰_목록을_조회한다() throws Exception {
        // given: 쿠폰 데이터 등록
        couponRepository.save(new Coupon(3L, "1000원 할인 쿠폰", 1000, 3));

        // 선행 발급
        mockMvc.perform(post("/api/coupon/1"))
                .andExpect(status().isOk());

        // 보유 쿠폰 목록 조회
        String response = mockMvc.perform(get("/api/coupon/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        List<CouponResponse> result = objectMapper.readValue(response, new TypeReference<>() {});
        assertThat(result).hasSize(1);
        assertThat(result.get(0).couponId()).isEqualTo(1L);
        assertThat(result.get(0).discountAmount()).isEqualTo(1000);
    }

}
