package kr.hhplus.be.server.application.coupon.usecase;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.stereotype.Service;

/**
 * 쿠폰 발급 UseCase
 * - 사용자가 쿠폰을 발급받지 않았고, 수량이 남아있는 경우 쿠폰을 발급한다.
 */
@Service
public class IssueCouponUseCase {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public IssueCouponUseCase(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    /**
     * 사용자에게 쿠폰을 발급
     * - 중복 발급 여부 확인
     * - 수량 확인 및 발급 처리
     */
    public CouponResponse execute(Long userId, Long couponId) {
        // 1. 중복 발급 확인
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        // 2. 쿠폰 유효성 및 발급 가능 여부 확인
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (!coupon.canIssue()) {
            throw new IllegalStateException("발급 가능한 쿠폰이 없습니다.");
        }

        // 3. 쿠폰 발급 처리
        coupon.issue();
        couponRepository.save(coupon);

        // 4. 유저-쿠폰 관계 저장
        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        // 5. 응답 반환
        return CouponResponse.from(userCoupon, coupon.getDiscountAmount());
    }
}