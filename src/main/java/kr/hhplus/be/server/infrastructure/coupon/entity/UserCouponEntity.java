package kr.hhplus.be.server.infrastructure.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_coupon")
public class UserCouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long couponId;

    private Long userId;

    private boolean isUsed;

    private LocalDateTime issuedAt;

    protected UserCouponEntity() {}

    public UserCouponEntity(Long couponId, Long userId, boolean isUsed, LocalDateTime issuedAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.issuedAt = issuedAt;
    }

    public static UserCouponEntity from(UserCoupon domain) {
        return new UserCouponEntity(domain.couponId(), domain.userId(), domain.isUsed(), domain.issuedAt());
    }

    public UserCoupon toDomain() {
        return new UserCoupon(couponId, userId, isUsed, issuedAt);
    }

}
