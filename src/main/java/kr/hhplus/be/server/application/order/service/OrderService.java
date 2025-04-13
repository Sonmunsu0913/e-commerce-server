package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final MockOrderReporter reporter;

    private long nextOrderId = 1000L;

    public OrderService(OrderRepository orderRepository,
        PointService pointService,
        MockOrderReporter reporter) {
        this.orderRepository = orderRepository;
        this.pointService = pointService;
        this.reporter = reporter;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        Order order = createOrder(request.getUserId(), request.getItems(), request.getCouponId());

        UserPoint updated = pointService.use(order.getUserId(), order.getFinalPrice());

        orderRepository.save(order);
        reporter.send(order.toResponse((int) updated.point()));

        return order.toResponse((int) updated.point());
    }

    private Order createOrder(Long userId, List<OrderItemRequest> items, Long couponId) {
        int discount = applyCouponPolicy(couponId);
        long orderId = generateOrderId();
        return new Order(orderId, userId, items, discount);
    }

    private long generateOrderId() {
        return nextOrderId++;
    }

    protected int applyCouponPolicy(Long couponId) {
        return (couponId != null && couponId.equals(101L)) ? 1000 : 0;
    }
}
