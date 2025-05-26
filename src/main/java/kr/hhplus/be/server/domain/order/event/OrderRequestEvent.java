package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.order.CreateOrderCommand;

/**
 * 주문 요청 이벤트.
 * 사용자의 주문 요청을 수신하여,
 * 쿠폰 검증 및 주문 생성을 유도하는 첫 번째 이벤트이다.
 */
public class OrderRequestEvent {

    private final OrderRequestedEventPayload payload;

    public OrderRequestEvent(OrderRequestedEventPayload payload) {
        this.payload = payload;
    }

    public OrderRequestedEventPayload getPayload() {
        return payload;
    }
}