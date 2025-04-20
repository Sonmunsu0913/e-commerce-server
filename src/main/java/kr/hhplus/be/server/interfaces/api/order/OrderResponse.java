package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class OrderResponse {

    @Schema(description = "주문 ID", example = "1234")
    private Long id;

    @Schema(description = "총 결제 금액", example = "6000")
    private Integer totalPrice;

    @Schema(description = "할인 금액", example = "1000")
    private Integer discount;

    @Schema(description = "최종 결제 금액", example = "5000")
    private Integer finalPrice;

    @Schema(description = "결제 후 남은 잔액", example = "8000")
    private Integer pointAfterPayment;

    @Schema(description = "주문 시간", example = "2025-04-02T14:30:00")
    private String orderedAt;

    public OrderResponse(Long id, Integer totalPrice, Integer discount, Integer finalPrice, Integer pointAfterPayment, String orderedAt) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.pointAfterPayment = pointAfterPayment;
        this.orderedAt = orderedAt;
    }

}
