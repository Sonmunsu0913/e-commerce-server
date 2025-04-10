package kr.hhplus.be.server.infrastructure.order.repository;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> orders = new HashMap<>();

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public void save(Order order) {
        orders.put(order.getOrderId(), order);
    }
}
