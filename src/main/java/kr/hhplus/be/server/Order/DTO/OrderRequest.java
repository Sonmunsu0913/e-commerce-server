package kr.hhplus.be.server.Order.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class OrderRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "주문 상품 목록")
    private List<OrderItem> items;

    @Schema(description = "사용할 쿠폰 ID (선택)", example = "101")
    private Long couponId;

    public Long getUserId() {
        return userId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Long getCouponId() {
        return couponId;
    }

    public static class OrderItem {
        @Schema(description = "상품 ID", example = "1")
        private Long productId;

        @Schema(description = "수량", example = "2")
        private Integer quantity;

        public Long getProductId() {
            return productId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }
}
