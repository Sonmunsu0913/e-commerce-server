package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.GetCouponService;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.order.service.ValidatePaymentService;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import kr.hhplus.be.server.domain.product.service.RecordProductSaleService;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.interfaces.api.order.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import kr.hhplus.be.server.domain.point.UserPoint;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

    @InjectMocks
    private OrderFacade orderFacade;

    @Mock private CreateOrderService createOrderService;
    @Mock private ValidatePaymentService validatePaymentService;
    @Mock private UsePointService usePointService;
    @Mock private GetUserPointService getUserPointService;
    @Mock private RecordProductSaleService recordProductSaleService;
    @Mock private GetUserCouponService getUserCouponService;
    @Mock private GetCouponService getCouponService;
    @Mock private MockOrderReporter reporter;

    @Test
    void 주문을_정상적으로_완료() {
        // given
        Long userId = 1L;
        Long couponId = null;  // 쿠폰 없음!
        List<OrderItemCommand> items = List.of(
                new OrderItemCommand(1L, "화과자", 5000, 3)
        );

        int discount = 0;
        int totalPrice = 15000;
        int finalPrice = totalPrice - discount;

        Order order = new Order(1L, userId, items, discount);
        UserPoint currentPointInfo = new UserPoint(userId, 20000, LocalDateTime.now(), LocalDateTime.now(), 0);
        UserPoint updatedPoint = new UserPoint(userId, 20000 - finalPrice, LocalDateTime.now(), LocalDateTime.now(), 0);

        given(getUserPointService.execute(userId)).willReturn(currentPointInfo);
        given(createOrderService.execute(userId, items, couponId)).willReturn(order);
        willDoNothing().given(validatePaymentService).execute(order, (int) currentPointInfo.point());
        given(usePointService.execute(userId, finalPrice)).willReturn(updatedPoint);
        willDoNothing().given(recordProductSaleService).execute(any());

        // when
        CreateOrderCommand command = new CreateOrderCommand(userId, items, couponId);
        OrderResult result = orderFacade.order(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(order.getId());
        assertThat(result.discount()).isEqualTo(discount);
        assertThat(result.finalPrice()).isEqualTo(finalPrice);
        assertThat(result.totalPrice()).isEqualTo(totalPrice);
        assertThat((int) updatedPoint.point()).isEqualTo(20000 - finalPrice);

        // reporter 호출 검증 (객체 내용 기준 비교)
        ArgumentCaptor<OrderResponse> captor = ArgumentCaptor.forClass(OrderResponse.class);
        then(reporter).should().send(captor.capture());

        OrderResponse actual = captor.getValue();
        OrderResponse expectedResponse = new OrderResponse(
                result.orderId(),
                result.totalPrice(),
                result.discount(),
                result.finalPrice(),
                20000 - finalPrice,
                result.orderedAt()
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedResponse);
    }


    @Test
    void 쿠폰이_있으면_할인이_적용() {
        // given
        Long userId = 1L;
        Long couponId = 101L;
        List<OrderItemCommand> items = List.of(
                new OrderItemCommand(1L, "화과자", 5000, 3)
        );
        OrderRequest request = new OrderRequest(userId, items, couponId);

        int discount = 1000;
        int totalPrice = 15000;
        int finalPrice = totalPrice - discount;

        Order order = new Order(1L, userId, items, discount);
        UserPoint currentPointInfo = new UserPoint(userId, 20000, LocalDateTime.now(), LocalDateTime.now(), 0);
        UserPoint updatedPoint = new UserPoint(userId, 20000 - finalPrice, LocalDateTime.now(), LocalDateTime.now(), 0);

        // ✨ 추가: 쿠폰, 유저쿠폰 생성
        UserCoupon userCoupon = new UserCoupon(couponId, userId, false, LocalDateTime.now());
        Coupon coupon = new Coupon(couponId, "테스트 쿠폰", discount, 100);

        // ✨ 추가: 쿠폰 관련 mock 세팅
        given(getUserCouponService.execute(userId, couponId)).willReturn(userCoupon);
        given(getCouponService.execute(couponId)).willReturn(coupon);

        // 기본 Mock 세팅
        given(getUserPointService.execute(userId)).willReturn(currentPointInfo);
        given(createOrderService.execute(userId, items, couponId)).willReturn(order);
        willDoNothing().given(validatePaymentService).execute(order, (int) currentPointInfo.point());
        given(usePointService.execute(userId, finalPrice)).willReturn(updatedPoint);
        willDoNothing().given(recordProductSaleService).execute(any());
        willDoNothing().given(reporter).send(any());

        // when
        CreateOrderCommand command = new CreateOrderCommand(userId, items, couponId);
        OrderResult result = orderFacade.order(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.orderId()).isEqualTo(order.getId());
        assertThat(result.discount()).isEqualTo(discount);
        assertThat(result.finalPrice()).isEqualTo(finalPrice);
        assertThat((int) updatedPoint.point()).isEqualTo(20000 - finalPrice);

        // reporter.send() 호출 검증 + 내용까지 검증
        ArgumentCaptor<OrderResponse> captor = ArgumentCaptor.forClass(OrderResponse.class);
        then(reporter).should().send(captor.capture());

        OrderResponse actual = captor.getValue();
        OrderResponse expected = new OrderResponse(
                result.orderId(),
                result.totalPrice(),
                result.discount(),
                result.finalPrice(),
                20000 - finalPrice,
                result.orderedAt()
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
