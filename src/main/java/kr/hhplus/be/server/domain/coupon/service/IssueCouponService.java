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
     * - 수량 확인 및 발급 처리 (비관적 락 사용)
     */
    public CouponResponse execute(Long userId) {
        // 1. 사용자가 이미 발급받은 미사용 쿠폰이 있는지 확인
        boolean hasUnusedCoupon = userCouponRepository
                .findAllByUserId(userId).stream()
                .anyMatch(userCoupon -> !userCoupon.isUsed());

        if (hasUnusedCoupon) {
            throw new IllegalStateException("이미 발급받은 미사용 쿠폰이 있습니다.");
        }

        // 2. 단일 쿠폰을 ID로 지정하여 비관적 락 걸고 조회
        Coupon coupon = couponRepository.findWithPessimisticLockById(1L);  // 💡 선착순 쿠폰 ID 명시

        // 3. 조건 검사 (수량 확인, 중복 확인)
        if (!coupon.canIssue()) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }

        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new IllegalStateException("이미 해당 쿠폰을 발급받았습니다.");
        }

        // 4. 쿠폰 발급 처리
        coupon.issue();
        couponRepository.save(coupon);

        // 5. 유저-쿠폰 관계 저장
        UserCoupon userCoupon = UserCoupon.create(userId, coupon.getId());
        userCouponRepository.save(userCoupon);

        // 6. 응답 반환
        return CouponResponse.from(userCoupon, coupon);
    }

}