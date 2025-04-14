package kr.hhplus.be.server.application.order.usecase;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;

    public CreateOrderUseCase(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Long userId, List<OrderItemRequest> items, Long couponId) {
        int discount = applyCouponPolicy(couponId);
        Long orderId = null; // ID는 DB에서 자동 생성 또는 외부에서 지정
        Order order = new Order(orderId, userId, items, discount);
        orderRepository.save(order);
        return order;
    }

    private int applyCouponPolicy(Long couponId) {
        return (couponId != null && couponId.equals(101L)) ? 1000 : 0;
    }
}
