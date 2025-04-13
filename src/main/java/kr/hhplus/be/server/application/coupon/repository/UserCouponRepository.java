package kr.hhplus.be.server.application.coupon.repository;

import kr.hhplus.be.server.domain.coupon.UserCoupon;

import java.util.List;

public interface UserCouponRepository {

    void save(UserCoupon userCoupon);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    List<UserCoupon> findAllByUserId(Long userId);

}
