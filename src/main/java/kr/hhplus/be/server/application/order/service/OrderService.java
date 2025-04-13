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
    private long nextOrderId = 1000L;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 주문 객체 생성 및 저장 (포인트 차감/외부 전송은 facade에서 수행)
     */
    public Order createOrder(Long userId, List<OrderItemRequest> items, Long couponId) {
        int discount = applyCouponPolicy(couponId);
        long orderId = generateOrderId();
        Order order = new Order(orderId, userId, items, discount);
        orderRepository.save(order);
        return order;
    }

    /**
     * 쿠폰 정책 적용: ID가 101이면 1000원 할인
     */
    protected int applyCouponPolicy(Long couponId) {
        return (couponId != null && couponId.equals(101L)) ? 1000 : 0;
    }

    private long generateOrderId() {
        return nextOrderId++;
    }

    /**
     * 주문 ID로 주문 조회 (Facade에서 결제 시 사용)
     */
    public Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }
}

