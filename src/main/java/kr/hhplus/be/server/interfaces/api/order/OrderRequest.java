package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "주문 상품 목록")
    private List<OrderItemCommand> items;

    @Schema(description = "사용할 쿠폰 ID (선택)", example = "101")
    private Long couponId;
}


