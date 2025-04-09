package kr.hhplus.be.server.application.order.service;

import java.time.LocalDateTime;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final OrderRepository orderRepository;
    private final PointService pointService;

    public PaymentService(OrderRepository orderRepository, PointService pointService) {
        this.orderRepository = orderRepository;
        this.pointService = pointService;
    }

    public PaymentResultResponse pay(Long orderId) {
        // 1. 주문 정보 조회
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 2. 포인트 잔액 확인 및 차감
        UserPoint userPoint = pointService.getPoint(order.getUserId());
        if (userPoint.point() < order.getFinalPrice()) {
            throw new IllegalStateException("포인트 부족");
        }

        UserPoint updated = pointService.use(order.getUserId(), order.getFinalPrice());

        // 3. 결제 결과 생성
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

