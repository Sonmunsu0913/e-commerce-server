package kr.hhplus.be.server.application.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouponResponse {

    @Schema(description = "쿠폰 ID", example = "101")
    private Long couponId;

    @Schema(description = "할인 금액", example = "2000")
    private Integer discountAmount;

    @Schema(description = "발급 시각", example = "2025-04-02T12:00:00")
    private String claimedAt;

    @Schema(description = "사용 여부", example = "false")
    private Boolean isUsed;

    public CouponResponse(Long couponId, Integer discountAmount, String claimedAt, Boolean isUsed) {
        this.couponId = couponId;
        this.discountAmount = discountAmount;
        this.claimedAt = claimedAt;
        this.isUsed = isUsed;
    }

    public Long getCouponId() {
        return couponId;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public String getClaimedAt() {
        return claimedAt;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }
}
