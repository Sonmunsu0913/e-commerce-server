package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
     * 사용자가 아직 발급받지 않았고, 잔여 수량이 남아있는 쿠폰 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 발급 가능한 쿠폰 목록 (잔여 수량 > 0, 사용자 미보유)
     */
    public List<CouponResponse> getAvailableCoupons(Long userId) {

        // 1. 사용자가 이미 발급받은 쿠폰 ID 목록 조회
        List<Long> alreadyIssuedCouponIds = userCouponRepository.findAllByUserId(userId).stream()
                .map(UserCoupon::couponId) // 발급받은 쿠폰 ID만 추출
                .collect(Collectors.toList());

        // 2. 전체 쿠폰 중에서:
        //   - 수량이 남아 있고 (canIssue = true)
        //   - 사용자가 이미 발급받지 않은 쿠폰만 필터링
        return couponRepository.findAllCoupon().stream()
                .filter(Coupon::canIssue)
                .filter(coupon -> !alreadyIssuedCouponIds.contains(coupon.getId()))
                .map(coupon -> CouponResponse.from(null, coupon)) // userCoupon 없이 coupon만 전달
                .toList();
    }


    /**
     * 사용자에게 쿠폰을 발급
     * - 중복 발급 여부 확인
     * - 수량 확인 및 발급 처리
     */
    public CouponResponse execute(Long userId, Long couponId) {
        // 1. 사용자가 이미 발급받은 미사용 쿠폰이 있는지 확인
        boolean hasUnusedCoupon = userCouponRepository
                .findAllByUserId(userId).stream()
                .anyMatch(userCoupon -> !userCoupon.isUsed());

        if (hasUnusedCoupon) {
            throw new IllegalStateException("이미 발급받은 미사용 쿠폰이 있습니다.");
        }

        // 2. 단일 쿠폰을 ID로 지정하여 비관적 락 걸고 조회
        Coupon coupon = couponRepository.findWithPessimisticLockById(couponId);  // 선착순 쿠폰 ID 명시
        if (coupon == null) {
            throw new IllegalArgumentException("존재하지 않는 쿠폰입니다.");
        }

        // 3. 조건 검사 (수량 확인, 중복 확인)
        if (!coupon.canIssue()) {
            throw new IllegalStateException("쿠폰 수량이 모두 소진되었습니다.");
        }

        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 해당 쿠폰을 발급받았습니다.");
        }

        // 4. 쿠폰 발급 처리
        coupon.issue();
        couponRepository.save(coupon);

        // 5. 유저-쿠폰 관계 저장
        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        // 6. 응답 반환
        return CouponResponse.from(userCoupon, coupon);
    }

}