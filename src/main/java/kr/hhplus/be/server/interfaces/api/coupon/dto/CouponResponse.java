package kr.hhplus.be.server.interfaces.api.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

public record CouponResponse(

    @Schema(description = "쿠폰 ID", example = "101")
    Long couponId,

    @Schema(description = "할인 금액", example = "2000")
    Integer discountAmount,

    @Schema(description = "발급 시각", example = "2025-04-02T12:00:00")
    LocalDateTime issuedAt,

    @Schema(description = "사용 여부", example = "false")
    Boolean isUsed

) {

    public String getName() {
        return "5천원 할인 쿠폰"; // 예시: 쿠폰 이름을 반환
    }

    public static CouponResponse from(UserCoupon userCoupon, int discountAmount) {
        return new CouponResponse(
            userCoupon.couponId(),
            discountAmount,
            userCoupon.issuedAt(),
            userCoupon.isUsed()
        );
    }
}
