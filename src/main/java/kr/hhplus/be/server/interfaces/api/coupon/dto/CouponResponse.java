package kr.hhplus.be.server.interfaces.api.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouponResponse {

    @Schema(description = "쿠폰 ID", example = "101")
    private Long couponId;

    @Schema(description = "할인 금액", example = "2000")
    private Integer discountAmount;

    @Schema(description = "발급 시각", example = "2025-04-02T12:00:00")
    private String issuedAt;

    @Schema(description = "사용 여부", example = "false")
    private Boolean isUsed;

    public CouponResponse(Long couponId, Integer discountAmount, String issuedAt, Boolean isUsed) {
        this.couponId = couponId;
        this.discountAmount = discountAmount;
        this.issuedAt = issuedAt;
        this.isUsed = isUsed;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }
}
