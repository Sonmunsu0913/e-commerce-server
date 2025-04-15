package kr.hhplus.be.server.infrastructure.order.repository;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.infrastructure.order.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderRepositoryImpl(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Order save(Order order) {
        OrderEntity saved = jpaOrderRepository.save(OrderEntity.from(order));
        return order.withOrderId(saved.getOrderId()); // 새로운 Order 리턴
    }

    @Override
    public Order findById(Long orderId) {
        return jpaOrderRepository.findById(orderId)
                .map(OrderEntity::toDomain)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문: " + orderId));
    }
}
