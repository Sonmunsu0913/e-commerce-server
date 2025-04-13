package kr.hhplus.be.server.application.order.service;

import java.time.LocalDate;
import java.util.*;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.product.service.ProductSaleService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Payment;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;

    public PaymentService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 주문 ID로 주문 조회 (존재하지 않으면 예외)
     */
    public Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }

    /**
     * 결제 가능 여부 확인
     * - 현재 포인트가 주문 결제 금액보다 많은지 검증
     */
    public void validatePayment(Order order, int currentPoint) {
        new Payment(order).validateEnoughPoint(currentPoint);
    }
}

