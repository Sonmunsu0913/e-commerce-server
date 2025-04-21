package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
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
        CreateOrderCommand command = new CreateOrderCommand(
            request.getUserId(),
            request.getItems(),
            request.getCouponId()
        );

        OrderResult result = orderFacade.order(command);

        return ResponseEntity.ok(new OrderResponse(
            result.orderId(),
            result.totalPrice(),
            result.discount(),
            result.finalPrice(),
            0,  // 결제 후 포인트 등 추가로 처리 가능
            result.orderedAt()
        ));
    }
}

