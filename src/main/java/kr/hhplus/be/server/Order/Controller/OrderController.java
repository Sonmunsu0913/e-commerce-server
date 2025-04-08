package kr.hhplus.be.server.Order.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import kr.hhplus.be.server.Order.DTO.OrderRequest;
import kr.hhplus.be.server.Order.DTO.OrderResponse;
import kr.hhplus.be.server.Order.Service.MockOrderReporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order", description = "주문 및 결제 API")
public class OrderController {

    private final MockOrderReporter reporter;

    public OrderController(MockOrderReporter reporter) {
        this.reporter = reporter;
    }
    @PostMapping
    @Operation(summary = "주문 및 결제", description = "상품을 주문하고 결제를 수행합니다.")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
        OrderResponse response = new OrderResponse(
            1234L,
            6000,
            1000,
            5000,
            8000,
            LocalDateTime.now().toString()
        );

        // 외부 전송
        reporter.send(response);

        // 외부 데이터 플랫폼 전송은 생략 (Mock 처리)
        return ResponseEntity.ok(response);
    }

}

