package kr.hhplus.be.server.domain.order;

public interface OrderRepository {

    // ID 기반 주문 조회
    Order findById(Long id);

    // 주문 저장
    Order save(Order order);

}

