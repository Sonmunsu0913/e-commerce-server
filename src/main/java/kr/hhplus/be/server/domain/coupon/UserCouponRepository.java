package kr.hhplus.be.server.domain.coupon;

import java.util.List;

public interface UserCouponRepository {

    void save(UserCoupon userCoupon);

    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    List<UserCoupon> findAllByUserId(Long userId);

}
