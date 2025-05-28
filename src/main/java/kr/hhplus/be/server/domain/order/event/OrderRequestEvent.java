package kr.hhplus.be.server.domain.order.event;

/**
 * 주문 요청 이벤트.
 * 사용자의 주문 요청을 수신하여,
 * 쿠폰 검증 및 주문 생성을 유도하는 첫 번째 이벤트이다.
 */
public class OrderRequestEvent {

    private final OrderRequestedEventData payload;

    public OrderRequestEvent(OrderRequestedEventData payload) {
        this.payload = payload;
    }

    public OrderRequestedEventData getPayload() {
        return payload;
    }
}