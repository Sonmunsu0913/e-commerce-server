package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import lombok.Getter;

@Getter
public class OrderRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemCommand> items;

    @Schema(description = "사용할 쿠폰 ID (선택)", example = "101")
    private Long couponId;

    public OrderRequest() {
        // Jackson 역직렬화용
    }

    public OrderRequest(Long userId, List<OrderItemCommand> items, Long couponId) {
        this.userId = userId;
        this.items = items;
        this.couponId = couponId;
    }

    public OrderRequest(Long userId, List<OrderItemCommand> items) {
        this.userId = userId;
        this.items = items;
        this.couponId = null;  // couponId가 선택적이므로 기본값은 null
    }
}


