package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@Tag(name = "Order", description = "주문 API")
public class OrderController {

    private final OrderService orderService;
    private final MockOrderReporter reporter;

    public OrderController(OrderService orderService, MockOrderReporter reporter) {
        this.orderService = orderService;
        this.reporter = reporter;
    }

    @PostMapping
    @Operation(summary = "주문 및 결제", description = "상품을 주문하고 결제를 수행합니다.")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {

        OrderResponse response = orderService.placeOrder(request);

        // 외부 데이터 플랫폼에 전송 (Mock)
        reporter.send(response);

        return ResponseEntity.ok(response);
    }
}

