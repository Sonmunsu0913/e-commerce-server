package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

/**
 * 사용자와 쿠폰 간의 발급 이력을 나타내는 모델
 * - 발급된 쿠폰 ID, 사용자 ID, 사용 여부, 발급 시각 포함
 */
public record UserCoupon(
    Long couponId,           // 발급된 쿠폰 ID
    Long userId,             // 쿠폰을 발급받은 사용자 ID
    boolean isUsed,          // 쿠폰 사용 여부
    LocalDateTime issuedAt   // 쿠폰 발급 시각
) {
    /**
     * UserCoupon 정적 생성 메서드
     * - 초기 발급 시 사용 여부는 false, 현재 시각으로 발급
     */
    public static UserCoupon create(Long userId, Long couponId) {
        return new UserCoupon(couponId, userId, false, LocalDateTime.now());
    }
}

