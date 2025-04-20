package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.Product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private CouponRepository couponRepository;

    private Long userId = 1L;

    @BeforeEach
    void setup() {
        pointRepository.save(new UserPoint(userId, 20000, LocalDateTime.now(), LocalDateTime.now()));
        productRepository.save(new Product(1L, "화과자", 5000, 5));
        couponRepository.save(new Coupon(101L, "1000원 할인 쿠폰", 1000, 10));
    }

    @Test
    void 실제_주문이_정상적으로_처리된다() {
        List<OrderItemCommand> items = List.of(
                new OrderItemCommand(1L, "고양이 화과자", 5000, 2)
        );
        OrderRequest request = new OrderRequest(userId, items, 101L);

        OrderResponse result = orderFacade.placeOrder(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getFinalPrice()).isEqualTo(9000);
        assertThat(result.getPointAfterPayment()).isEqualTo(11000);
    }
}
