package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.service.PaymentService;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}")
    @Operation(summary = "주문 결제", description = "주문 ID를 기반으로 결제를 수행합니다.")
    public ResponseEntity<PaymentResultResponse> pay(@PathVariable Long orderId) {
        PaymentResultResponse result = paymentService.pay(orderId);
        return ResponseEntity.ok(result);
    }
}
