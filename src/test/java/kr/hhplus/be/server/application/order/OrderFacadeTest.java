package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.service.OrderFacade;
import kr.hhplus.be.server.application.order.usecase.CreateOrderUseCase;
import kr.hhplus.be.server.application.order.usecase.ValidatePaymentUseCase;
import kr.hhplus.be.server.application.point.usecase.GetUserPointUseCase;
import kr.hhplus.be.server.application.point.usecase.UsePointUseCase;
import kr.hhplus.be.server.application.product.usecase.RecordProductSaleUseCase;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.order.Order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock private CreateOrderUseCase createOrderUseCase;
    @Mock private ValidatePaymentUseCase validatePaymentUseCase;
    @Mock private UsePointUseCase usePointUseCase;
    @Mock private GetUserPointUseCase getUserPointUseCase;
    @Mock private RecordProductSaleUseCase recordProductSaleUseCase;
    @Mock private MockOrderReporter reporter;

    @Test
    void 주문을_정상적으로_완료하면_리포팅까지_진행된다() {
        // given
        Long userId = 1L;
        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(1L, "화과자", 5000, 3)
        );
        OrderRequest request = new OrderRequest(userId, items, null);

        Order order = new Order(1L, userId, items, 0);

        UserPoint currentPointInfo = new UserPoint(userId, 10000, LocalDateTime.now(), LocalDateTime.now());
        UserPoint updatedPoint = new UserPoint(userId, 5000, LocalDateTime.now(), LocalDateTime.now());

        // mocking
        given(getUserPointUseCase.execute(userId)).willReturn(currentPointInfo);
        given(createOrderUseCase.execute(userId, items, null)).willReturn(order);
        willDoNothing().given(validatePaymentUseCase).execute(order, (int) currentPointInfo.point());
        given(usePointUseCase.execute(userId, order.getFinalPrice())).willReturn(updatedPoint);
        willDoNothing().given(recordProductSaleUseCase).execute(any());
        willDoNothing().given(reporter).send(any());

        // when
        OrderResponse result = orderFacade.placeOrder(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.getPointAfterPayment()).isEqualTo((int) updatedPoint.point());
        then(reporter).should().send(result);
    }

    @Test
    void 쿠폰이_있으면_할인이_적용되고_리포팅까지_진행된다() {
        // given
        Long userId = 1L;
        Long couponId = 101L;
        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(1L, "화과자", 5000, 3) // 총 15,000원
        );
        OrderRequest request = new OrderRequest(userId, items, couponId);

        int discount = 1000;
        int totalPrice = 15000;
        int finalPrice = totalPrice - discount;

        Order order = new Order(1L, userId, items, discount);
        UserPoint currentPointInfo = new UserPoint(userId, 20000, LocalDateTime.now(), LocalDateTime.now());
        UserPoint updatedPoint = new UserPoint(userId, 20000 - finalPrice, LocalDateTime.now(), LocalDateTime.now());

        // mocking
        given(getUserPointUseCase.execute(userId)).willReturn(currentPointInfo);
        given(createOrderUseCase.execute(userId, items, couponId)).willReturn(order);
        willDoNothing().given(validatePaymentUseCase).execute(order, (int) currentPointInfo.point());
        given(usePointUseCase.execute(userId, order.getFinalPrice())).willReturn(updatedPoint);
        willDoNothing().given(recordProductSaleUseCase).execute(any());
        willDoNothing().given(reporter).send(any());

        // when
        OrderResponse result = orderFacade.placeOrder(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(result.getDiscount()).isEqualTo(discount);
        assertThat(result.getFinalPrice()).isEqualTo(finalPrice);
        assertThat(result.getPointAfterPayment()).isEqualTo((int) updatedPoint.point());
        then(reporter).should().send(result);
    }

}
