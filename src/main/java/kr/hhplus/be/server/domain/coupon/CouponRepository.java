package kr.hhplus.be.server.domain.coupon;

import java.util.*;

public interface CouponRepository {

    List<Coupon> findAllCoupons();

    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);
}
