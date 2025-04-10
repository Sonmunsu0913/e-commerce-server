package kr.hhplus.be.server.domain.coupon;

public class UserCoupon {

    private final Long couponId;
    private final Long userId;
    private final boolean isUsed;
    private final String claimedAt;

    public UserCoupon(Long couponId, Long userId, boolean isUsed, String claimedAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.claimedAt = claimedAt;
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

    public String getClaimedAt() {
        return claimedAt;
    }
}
