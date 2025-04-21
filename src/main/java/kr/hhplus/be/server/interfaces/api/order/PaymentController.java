package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.PaymentResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final OrderFacade orderFacade;

    public PaymentController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping("/{id}")
    @Operation(summary = "주문 결제", description = "주문 ID를 기반으로 결제를 수행합니다.")
    public ResponseEntity<PaymentResponse> pay(@PathVariable Long id) {
        // 1. Application Layer의 결제 결과 수신
        PaymentResult result = orderFacade.pay(id);

        // 2. Controller에서 Presentation DTO로 매핑
        PaymentResponse response = new PaymentResponse(
                result.order().orderId(),
                result.order().totalPrice(),
                result.order().discount(),
                result.order().finalPrice(),
                result.pointAfterPayment(),
                result.order().orderedAt()
        );

        // 3. HTTP 200 OK 응답 반환
        return ResponseEntity.ok(response);
    }
}
