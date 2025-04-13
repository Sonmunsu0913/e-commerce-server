package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.order.service.OrderFacade;
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

    private final OrderFacade orderFacade;

    public OrderController(OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @PostMapping
    @Operation(summary = "주문 및 결제", description = "상품을 주문하고 결제를 수행합니다.")
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
        // 주문 + 결제 + 포인트 차감 + 상품 판매기록 + 전송까지 모두 Facade에서 처리
        OrderResponse response = orderFacade.placeOrder(request);
        return ResponseEntity.ok(response);
    }
}

