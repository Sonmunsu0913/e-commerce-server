package kr.hhplus.be.server.application.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.point.usecase.GetUserPointUseCase;
import kr.hhplus.be.server.application.point.usecase.UsePointUseCase;
import kr.hhplus.be.server.application.product.service.ProductSaleService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
import org.springframework.stereotype.Service;

@Service
public class OrderFacade {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UsePointUseCase usePointUseCase;
    private final GetUserPointUseCase getUserPointUseCase;
    private final ProductSaleService productSaleService;
    private final MockOrderReporter reporter;

    public OrderFacade(OrderService orderService, PaymentService paymentService,
                       UsePointUseCase usePointUseCase, GetUserPointUseCase getUserPointUseCase,
                       ProductSaleService productSaleService, MockOrderReporter reporter) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.usePointUseCase = usePointUseCase;
        this.getUserPointUseCase = getUserPointUseCase;
        this.productSaleService = productSaleService;
        this.reporter = reporter;
    }

    public OrderResponse placeOrder(OrderRequest request) {
        // 1. 주문 생성
        Order order = orderService.createOrder(
                request.getUserId(),
                request.getItems(),
                request.getCouponId()
        );

        // 2. 사용자 현재 포인트 조회
        UserPoint current = getUserPointUseCase.execute(order.getUserId());

        // 3. 결제 가능 여부 검증 (포인트 부족 시 예외 발생)
        paymentService.validatePayment(order, (int) current.point());

        // 4. 포인트 차감
        UserPoint updated = usePointUseCase.execute(order.getUserId(), order.getFinalPrice());

        // 5. 판매 기록 저장
        LocalDate today = LocalDate.now();
        for (OrderItemRequest item : request.getItems()) {
            productSaleService.recordSale(new ProductSale(item.productId(), today, item.quantity()));
        }

        // 6. 응답 생성 및 외부 시스템 전송 (mock)
        OrderResponse response = order.toResponse((int) updated.point());
        reporter.send(response);

        return response;
    }

    public PaymentResultResponse pay(Long orderId) {
        // 1. 주문 ID로 주문 조회 (존재하지 않으면 예외 발생)
        Order order = paymentService.getOrderOrThrow(orderId);

        // 2. 사용자 현재 포인트 조회
        UserPoint current = getUserPointUseCase.execute(order.getUserId());

        // 3. 포인트가 결제 금액보다 충분한지 검증
        paymentService.validatePayment(order, (int) current.point());

        // 4. 포인트 차감 (결제)
        UserPoint updated = usePointUseCase.execute(order.getUserId(), order.getFinalPrice());

        // 5. 각 상품별 판매 기록 저장
        for (OrderItemRequest item : order.getItems()) {
            productSaleService.recordSale(
                    new ProductSale(item.productId(), LocalDate.now(), item.quantity())
            );
        }

        // 6. 결제 결과 응답 생성 후 반환
        return new PaymentResultResponse(
                order.getOrderId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                (int) updated.point(),               // 결제 후 잔액
                LocalDateTime.now().toString()       // 결제 시각
        );
    }
}

