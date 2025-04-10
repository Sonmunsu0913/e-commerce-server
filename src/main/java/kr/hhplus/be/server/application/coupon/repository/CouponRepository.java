package kr.hhplus.be.server.application.coupon.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.Coupon;

public interface CouponRepository {

    Optional<Coupon> findById(Long couponId);

}
