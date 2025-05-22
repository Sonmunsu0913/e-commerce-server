package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;

/**
 * 주문 판매 처리 이벤트.
 * 판매 기록 저장 및 랭킹 반영에 사용된다.
 */
public class OrderSaleEvent {

    private final Order order;

    public OrderSaleEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
