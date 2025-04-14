package kr.hhplus.be.server.application.order.repository;

import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {

    // ID 기반 주문 조회
    Order findById(Long orderId);

    // 주문 저장
    void save(Order order);

}

