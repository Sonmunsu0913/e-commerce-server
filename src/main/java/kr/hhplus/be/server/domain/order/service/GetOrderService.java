package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Service;

@Service
public class GetOrderService {

    private final OrderRepository orderRepository;

    public GetOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order execute(Long id) {
        Order order = orderRepository.findById(id);
        if (order == null) {
            throw new IllegalArgumentException("존재하지 않는 주문입니다.");
        }
        return order;
    }
}

