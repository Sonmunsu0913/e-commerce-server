package kr.hhplus.be.server.domain.point.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;

/**
 * 포인트 차감 완료 후 발행되는 이벤트.
 */
public class PointEvent {

    private final Order order;
    private final UserPoint point;

    public PointEvent(Order order, UserPoint point) {
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
