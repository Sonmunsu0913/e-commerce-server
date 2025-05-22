package kr.hhplus.be.server.domain.point.event;

import kr.hhplus.be.server.domain.order.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import kr.hhplus.be.server.domain.order.service.ValidatePaymentService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 생성 이후 포인트 검증 및 차감 처리를 담당하는 리스너.
 * - OrderCreatedEvent 수신
 * - 포인트 조회 및 검증
 * - 포인트 차감
 * - PointEvent 발행
 */
@Component
public class PointEventListener {

    private final GetUserPointService getUserPointService;
    private final ValidatePaymentService validatePaymentService;
    private final UsePointService usePointService;
    private final OrderEventPublisher orderEventPublisher;

    public PointEventListener(GetUserPointService getUserPointService,
                              ValidatePaymentService validatePaymentService,
                              UsePointService usePointService,
                              OrderEventPublisher orderEventPublisher) {
        this.getUserPointService = getUserPointService;
        this.validatePaymentService = validatePaymentService;
        this.usePointService = usePointService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Transactional
    @EventListener
    public void handle(OrderCreatedEvent event) {
        Order order = event.getOrder();

        // 1. 사용자 현재 포인트 조회
        UserPoint current = getUserPointService.execute(order.getUserId());

        // 2. 결제 가능 여부 검증
        validatePaymentService.execute(order, (int) current.point());

        // 3. 포인트 차감
        UserPoint updated = usePointService.execute(order.getUserId(), order.getFinalPrice());

        // 4. 포인트 이벤트 발행
        orderEventPublisher.publishPoint(order, updated);
    }
}

