package kr.hhplus.be.server.application.order.repository;

import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {

    // ID 기반 주문 조회
    Order findById(Long id);

    // 주문 저장
    Order save(Order order);

}

