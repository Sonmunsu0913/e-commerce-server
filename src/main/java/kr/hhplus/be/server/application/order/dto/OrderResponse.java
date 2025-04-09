package kr.hhplus.be.server.application.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderResponse {

    @Schema(description = "주문 ID", example = "1234")
    private Long orderId;

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

    public OrderResponse(Long orderId, Integer totalPrice, Integer discount, Integer finalPrice, Integer pointAfterPayment, String orderedAt) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.pointAfterPayment = pointAfterPayment;
        this.orderedAt = orderedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }

    public Integer getPointAfterPayment() {
        return pointAfterPayment;
    }

    public String getOrderedAt() {
        return orderedAt;
    }
}
