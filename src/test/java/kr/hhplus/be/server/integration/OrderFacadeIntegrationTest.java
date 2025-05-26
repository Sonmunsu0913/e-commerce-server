package kr.hhplus.be.server.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 클래스 레벨의 @Transactional은 유지하여 기본 롤백 정책을 따르도록 합니다.
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserPointRepository userPointRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // TestConfiguration을 사용하여 Spy 객체를 빈으로 등록합니다.
    @TestConfiguration
    static class OrderFacadeIntegrationTestConfig {
        @Bean
        @Primary // 실제 MockOrderReporter 빈 대신 이 spy 빈을 우선적으로 사용하도록 합니다.
        public MockOrderReporter spyMockOrderReporter(ObjectMapper objectMapper) {
            // MockOrderReporter가 ObjectMapper를 의존하는 경우를 고려하여 생성자에 주입합니다.
            // 만약 기본 생성자로 충분하다면 new MockOrderReporter()로 변경합니다.
            // MockOrderReporter의 실제 생성자에 맞춰주세요.
            // 현재 MockOrderReporter는 기본 생성자를 가지고 있으므로 new MockOrderReporter()로 충분합니다.
            return Mockito.spy(new MockOrderReporter());
        }
    }

    @Autowired // TestConfig에서 등록한 spy 빈을 주입받습니다.
    private MockOrderReporter mockOrderReporter;

    private Long userId = 1L;

    @BeforeEach
    void setup() {
        userPointRepository.save(new UserPoint(userId, 20000, LocalDateTime.now(), LocalDateTime.now(), 0));
        // 상품 ID와 쿠폰 ID를 명시적으로 지정하여 테스트 간 일관성을 높입니다.
        productRepository.save(new Product(1L, "화과자", 5000, 5));
        couponRepository.save(new Coupon(101L, "1000원 할인 쿠폰", 1000, 10));
        userCouponRepository.save(UserCoupon.create(userId, 101L));
    }

    @Test
    @Commit // 이 테스트는 트랜잭션을 커밋하여 AFTER_COMMIT 리스너가 동작하도록 합니다.
    void 실제_주문이_정상적으로_처리된다() {
        // given
        List<OrderItemCommand> items = List.of(
            new OrderItemCommand(1L, "화과자", 5000, 2) // @BeforeEach에서 생성한 상품 ID
        );
        OrderRequest request = new OrderRequest(this.userId, items, 101L); // @BeforeEach에서 생성한 쿠폰 ID

        CreateOrderCommand command = new CreateOrderCommand(
                request.getUserId(),
                request.getItems(),
                request.getCouponId()
        );

        // when
        OrderResult result = orderFacade.order(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isNotNull();
        assertThat(result.totalPrice()).isEqualTo(10000);
        assertThat(result.discount()).isEqualTo(1000);
        assertThat(result.finalPrice()).isEqualTo(9000);
        assertThat(result.pointAfterPayment()).isEqualTo(11000); // 20000 - 9000

    }

    @Test
    @Commit // 이 테스트도 트랜잭션을 커밋하여 AFTER_COMMIT 리스너가 동작하도록 합니다.
    void 실제_주문이_정상적으로_처리된다_v2() throws Exception {
        // given
        OrderRequest request = new OrderRequest(
            this.userId,
            List.of(new OrderItemCommand(1L, "화과자", 5000, 2)), // @BeforeEach에서 생성한 상품 ID
            101L // @BeforeEach에서 생성한 쿠폰 ID
        );

        // when & then
        mockMvc.perform(post("/api/order/v2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isAccepted())
            .andExpect(content().string("주문 요청이 접수되었습니다."));

    }
}