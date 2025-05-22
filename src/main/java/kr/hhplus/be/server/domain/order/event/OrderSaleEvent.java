package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;

/**
 * 주문 판매 처리 이벤트.
 * 판매 기록 저장 및 랭킹 반영에 사용된다.
 */
public class OrderSaleEvent {
    private final Order order;
    private final UserPoint point;

    public OrderSaleEvent(Order order, UserPoint point) {
        this.order = order;
        this.point = point;
    }

    public Order getOrder() {
        return order;
    }

    public UserPoint getPoint() {
        return point;
    }
}

