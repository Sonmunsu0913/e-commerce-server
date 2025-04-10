package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public class UserCoupon {

    private final Long couponId;
    private final Long userId;
    private final boolean isUsed;
    private final String issuedAt;

    public UserCoupon(Long couponId, Long userId, boolean isUsed, String issuedAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public static UserCoupon create(Long userId, Long couponId) {
        return new UserCoupon(
                couponId,
                userId,
                false,
                LocalDateTime.now().toString()
        );
    }

    public Long getCouponId() {
        return couponId;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public String getIssuedAt() {
        return issuedAt;
    }
}
