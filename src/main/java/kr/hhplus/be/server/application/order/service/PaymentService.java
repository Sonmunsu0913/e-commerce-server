package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Payment;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PointService pointService;

    public PaymentService(OrderRepository orderRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.pointService = pointService;
    }

    public PaymentResultResponse pay(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        UserPoint current = pointService.getPoint(order.getUserId());

        Payment payment = new Payment(order);
        payment.validateEnoughPoint((int) current.point());

        UserPoint updated = pointService.use(order.getUserId(), order.getFinalPrice());

        return new PaymentResultResponse(
                order.getOrderId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                (int) updated.point(),
                LocalDateTime.now().toString()
        );
    }
}

