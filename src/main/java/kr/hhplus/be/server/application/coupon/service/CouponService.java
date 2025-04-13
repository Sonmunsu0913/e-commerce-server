package kr.hhplus.be.server.application.coupon.service;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;


    public CouponService(CouponRepository couponRepository,
        UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public List<CouponResponse> getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(userId);

        return userCoupons.stream()
            .map(uc -> {
                Coupon coupon = couponRepository.findById(uc.couponId())
                    .orElse(null); // 예외 던지지 않고 0으로 처리
                return CouponResponse.from(uc, coupon != null ? coupon.getDiscountAmount() : 0);
            })
            .toList();
    }

    public CouponResponse issueCoupon(Long userId, Long couponId) {
        // 1. 유저가 이미 쿠폰을 발급받았는지 확인
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        // 2. 쿠폰 존재 여부 및 발급 가능 여부 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!coupon.canIssue()) {
            throw new IllegalStateException("발급 가능한 쿠폰이 없습니다.");
        }

        // 3. 발급 처리
        coupon.issue(); // 발급 수량 증가
        couponRepository.save(coupon); // 저장 (DB 연동 시 필요)

        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        // 4. 응답 반환
        return CouponResponse.from(userCoupon, coupon.getDiscountAmount());
    }

}
