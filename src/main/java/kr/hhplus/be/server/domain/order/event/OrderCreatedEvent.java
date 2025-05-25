package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;

/**
 * 주문 생성 완료 이벤트.
 * 주문이 정상적으로 생성된 후, 이후 포인트 차감 등 다음 처리를 위한 이벤트이다.
 */
public class OrderCreatedEvent {

    private final Order order;

    public OrderCreatedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}