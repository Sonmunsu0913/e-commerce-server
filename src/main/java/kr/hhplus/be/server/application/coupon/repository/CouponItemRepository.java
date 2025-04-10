package kr.hhplus.be.server.application.coupon.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.CouponItem;

public interface CouponItemRepository {

    Optional<CouponItem> findFirstByCouponTypeAndIssuedFalse(Long couponId);

    void save(CouponItem couponItem);

}
