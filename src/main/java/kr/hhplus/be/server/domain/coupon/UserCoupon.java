package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public record UserCoupon(
    Long couponId,
    Long userId,
    boolean isUsed,
    LocalDateTime issuedAt
) {
    public static UserCoupon create(Long userId, Long couponId) {
        return new UserCoupon(couponId, userId, false, LocalDateTime.now());
    }
}
