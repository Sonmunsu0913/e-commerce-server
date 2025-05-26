package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.GetCouponService;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.coupon.service.ValidateCouponService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
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

    private final ValidateCouponService validateCouponService;
    private final CreateOrderService createOrderService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderRequestEventListener(ValidateCouponService validateCouponService,
                                     CreateOrderService createOrderService,
                                     OrderEventPublisher orderEventPublisher) {
        this.validateCouponService = validateCouponService;
        this.createOrderService = createOrderService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderRequestEvent event) {
        CreateOrderCommand command = event.getCommand();

        // 1. 쿠폰 검증
        validateCouponService.validate(command.userId(), command.couponId());

        // 2. 주문 생성
        Order order = createOrderService.execute(command.userId(), command.items(), command.couponId());

        // 3. 주문 생성 완료 이벤트 발행
        orderEventPublisher.publishCreated(order);
    }
}
