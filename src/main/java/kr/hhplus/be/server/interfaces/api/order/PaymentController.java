package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.facade.OrderFacade;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;
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

    @PostMapping("/{orderId}")
    @Operation(summary = "주문 결제", description = "주문 ID를 기반으로 결제를 수행합니다.")
    public ResponseEntity<PaymentResultResponse> pay(@PathVariable Long orderId) {
        // 향후 별도 결제 처리 로직이 필요할 경우 확장 가능
        PaymentResultResponse result = orderFacade.pay(orderId);
        return ResponseEntity.ok(result);
    }
}
