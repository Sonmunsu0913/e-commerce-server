package kr.hhplus.be.server.interfaces.api.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import lombok.Getter;

@Getter
public class OrderRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemRequest> items;

    @Schema(description = "사용할 쿠폰 ID (선택)", example = "101")
    private Long couponId;

    public OrderRequest(Long userId, List<OrderItemRequest> items, Long couponId) {
        this.userId = userId;
        this.items = items;
        this.couponId = couponId;
    }

    public OrderRequest(Long userId, List<OrderItemRequest> items) {
        this.userId = userId;
        this.items = items;
        this.couponId = null;  // couponId가 선택적이므로 기본값은 null
    }
}


