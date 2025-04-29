package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class GetCouponService {

    private final CouponRepository couponRepository;

    public GetCouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    /**
     * 주어진 couponId로 Coupon 조회
     *
     * @param couponId 조회할 쿠폰 ID
     * @return Coupon 도메인 객체
     * @throws IllegalArgumentException 쿠폰이 존재하지 않는 경우
     */
    public Coupon execute(Long couponId) {
        return couponRepository.findById(couponId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다: " + couponId));
    }
}
