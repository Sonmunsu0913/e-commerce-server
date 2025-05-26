package kr.hhplus.be.server.domain.product.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;

/**
 * 주문 판매 처리 이벤트.
 * 판매 기록 저장 및 랭킹 반영에 사용된다.
 */
public class ProductSaleEvent {
    private final Order order;

    public ProductSaleEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

}

