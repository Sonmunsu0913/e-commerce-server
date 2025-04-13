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
    private final PointService pointService;
    private final ProductSaleService productSaleService;

    public PaymentService(OrderRepository orderRepository,
        PointService pointService,
        ProductSaleService productSaleService) {
        this.orderRepository = orderRepository;
        this.pointService = pointService;
        this.productSaleService = productSaleService;
    }

    // 주문 ID 기반으로 결제를 진행하고 결과 반환
    public PaymentResultResponse pay(Long orderId) {
        Order order = getOrderOrThrow(orderId);
        validatePayment(order); // 포인트 충분한지 확인
        UserPoint updated = pointService.use(order.getUserId(), order.getFinalPrice());

        recordSales(order.getItems()); // 상품 판매 기록
        return toPaymentResult(order, updated.point());
    }

    // 존재하지 않는 주문일 경우 예외
    private Order getOrderOrThrow(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));
    }

    // 결제 가능 여부 확인 (잔액 부족 시 예외)
    private void validatePayment(Order order) {
        Payment payment = new Payment(order);
        UserPoint current = pointService.getPoint(order.getUserId());
        payment.validateEnoughPoint((int) current.point());
    }

    // 각 상품별로 판매 이력 기록
    private void recordSales(List<OrderItemRequest> items) {
        LocalDate today = LocalDate.now();
        for (OrderItemRequest item : items) {
            productSaleService.recordSale(
                new ProductSale(item.productId(), today, item.quantity())
            );
        }
    }

    // 응답 포맷 생성
    private PaymentResultResponse toPaymentResult(Order order, long pointAfterPayment) {
        return new PaymentResultResponse(
            order.getOrderId(),
            order.getTotalPrice(),
            order.getDiscount(),
            order.getFinalPrice(),
            (int) pointAfterPayment,
            LocalDateTime.now().toString()
        );
    }
}
