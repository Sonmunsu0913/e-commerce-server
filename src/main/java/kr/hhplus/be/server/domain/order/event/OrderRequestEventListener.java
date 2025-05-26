package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.GetCouponService;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.coupon.service.ValidateCouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.order.service.GetOrderService;
import kr.hhplus.be.server.domain.point.event.PointEventListener;
import kr.hhplus.be.server.domain.point.event.PointEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * OrderRequestEvent 수신 후,
 * - 쿠폰 검증
 * - 주문 생성
 * 을 처리하고 다음 단계로 OrderCreatedEvent를 발행하는 리스너.
 */
@Component
public class OrderRequestEventListener {

    private final GetOrderService getOrderService;
    private final PointEventPublisher pointEventPublisher;

    public OrderRequestEventListener(GetOrderService getOrderService,
                                     PointEventPublisher pointEventPublisher) {
        this.getOrderService = getOrderService;
        this.pointEventPublisher = pointEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderRequestEvent event) {
        OrderRequestedEventPayload payload = event.getPayload();

        // 1. 주문 재조회 (주문 ID 기준)
        Order order = getOrderService.execute(payload.orderId());

        // 2. 포인트 차감 등 후속 작업을 위한 이벤트 발행
        pointEventPublisher.publishPoint(order);
    }
}
