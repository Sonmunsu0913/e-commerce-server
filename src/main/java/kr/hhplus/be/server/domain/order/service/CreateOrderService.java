package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateOrderService {

    private final OrderRepository orderRepository;

    public CreateOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Long userId, List<OrderItemCommand> items, Long couponId) {
        int discount = applyCouponPolicy(couponId);
        Order order = new Order(null, userId, items, discount);
        return orderRepository.save(order); // 저장된 order 반환 (with ID)
    }

    private int applyCouponPolicy(Long couponId) {
        return (couponId != null && couponId.equals(101L)) ? 1000 : 0;
    }
}
