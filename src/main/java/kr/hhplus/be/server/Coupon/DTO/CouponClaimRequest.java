package kr.hhplus.be.server.Coupon.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class CouponClaimRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

