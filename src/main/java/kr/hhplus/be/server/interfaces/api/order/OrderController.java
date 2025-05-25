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
                result.pointAfterPayment(),
                result.orderedAt()
        ));
    }

    @PostMapping("/v2")
    @Operation(summary = "주문 요청 (이벤트 기반)", description = "이벤트 기반으로 주문 요청을 처리합니다.")
    public ResponseEntity<String> order2(@RequestBody OrderRequest request) {
        CreateOrderCommand command = new CreateOrderCommand(
            request.getUserId(),
            request.getItems(),
            request.getCouponId()
        );

        // 이벤트 발행 (비동기 처리 시작)
        orderFacade.orderV2(command);

        // 클라이언트에게 비동기 처리 메시지 반환
        return ResponseEntity.accepted().body("주문 요청이 접수되었습니다.");
    }
}

