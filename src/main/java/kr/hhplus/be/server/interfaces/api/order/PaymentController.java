package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.dto.PaymentResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@Tag(name = "Payment", description = "결제 API")
public class PaymentController {

    @PostMapping("/{orderId}")
    @Operation(summary = "주문 결제", description = "주문 ID를 기반으로 결제를 수행합니다.")
    public ResponseEntity<PaymentResultResponse> pay(@PathVariable Long orderId) {
        // 잔액 확인 → 결제 로직 수행 → 결과 반환
        return ResponseEntity.ok(null);
    }
}

