package kr.hhplus.be.server.domain.coupon;

import java.util.*;

public interface CouponRepository {

    List<Coupon> findAllCoupon();

    Optional<Coupon> findById(Long couponId);

    void save(Coupon coupon);

    Coupon findWithPessimisticLockById(Long couponId);  // 비관적 락 메서드

    Coupon findWithOptimisticLockById(Long couponId);  // 낙관적 락 메서드

    boolean existsById(Long couponId);

}
