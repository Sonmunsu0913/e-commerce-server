package kr.hhplus.be.server.application.order.service;

import java.util.*;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointService pointService;

    private long nextOrderId = 1000L;

    public OrderService(OrderRepository orderRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.pointService = pointService;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        // 주문 생성
        Order order = createOrder(request.getUserId(), request.getItems(), request.getCouponId());

        // 결제 (포인트 차감)
        UserPoint updated = pointService.use(order.getUserId(), order.getFinalPrice());

        // 저장
        orderRepository.save(order);

        // 주문 객체 반환
        return order.toResponse((int) updated.point());
    }

    private Order createOrder(Long userId, List<OrderItemRequest> items, Long couponId) {
        int discount = applyCoupon(couponId);
        Long orderId = generateOrderId();

        return new Order(orderId, userId, items, discount);
    }

    private int applyCoupon(Long couponId) {
        if (couponId != null && couponId.equals(101L)) {
            return 1000;
        }
        return 0;
    }

    private Long generateOrderId() {
        return nextOrderId++;
    }
}



