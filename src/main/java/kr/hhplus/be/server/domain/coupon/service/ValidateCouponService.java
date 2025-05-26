package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.stereotype.Service;

@Service
public class ValidateCouponService {

    private final GetUserCouponService getUserCouponService;
    private final GetCouponService getCouponService;

    public ValidateCouponService(GetUserCouponService getUserCouponService,
                                 GetCouponService getCouponService) {
        this.getUserCouponService = getUserCouponService;
        this.getCouponService = getCouponService;
    }

    public void validate(Long userId, Long couponId) {
        if (couponId == null) return;

        UserCoupon userCoupon = getUserCouponService.execute(userId, couponId);
        if (userCoupon.isUsed()) {
            throw new IllegalStateException("이미 사용한 쿠폰입니다.");
        }

        getCouponService.execute(couponId); // 존재 여부 검증
    }
}