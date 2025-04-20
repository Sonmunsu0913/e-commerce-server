package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 쿠폰 발급 UseCase
 * - 사용자가 쿠폰을 발급받지 않았고, 수량이 남아있는 경우 쿠폰을 발급한다.
 */
@Service
@Transactional
public class IssueCouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public IssueCouponService(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    /**
     * 사용자에게 쿠폰을 발급
     * - 중복 발급 여부 확인
     * - 수량 확인 및 발급 처리
     */
    public CouponResponse execute(Long userId) {

        // 1. 사용자가 이미 발급받은 미사용 쿠폰이 있는지 확인
        boolean hasUnusedCoupon = userCouponRepository
                .findAllByUserId(userId).stream()
                .anyMatch(userCoupon -> !userCoupon.isUsed());

        if (hasUnusedCoupon) {
            throw new IllegalStateException("이미 발급받은 미사용 쿠폰이 있습니다.");
        }

        // 2. 발급 가능한 쿠폰 찾기 (아직 발급받지 않았고 수량이 남은 쿠폰)
        Coupon coupon = couponRepository.findAllCoupons().stream()
                .filter(c -> c.canIssue() && !userCouponRepository.existsByUserIdAndCouponId(userId, c.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("발급 가능한 쿠폰이 없습니다."));

        // 3. 쿠폰 발급 처리
        coupon.issue();
        couponRepository.save(coupon);

        // 4. 유저-쿠폰 관계 저장
        UserCoupon userCoupon = UserCoupon.create(userId, coupon.getId());
        userCouponRepository.save(userCoupon);

        // 5. 응답 반환
        return CouponResponse.from(userCoupon, coupon);
    }
}