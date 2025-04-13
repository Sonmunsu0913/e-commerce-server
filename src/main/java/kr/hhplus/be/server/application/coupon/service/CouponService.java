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

    /**
     * 사용자 보유 쿠폰 목록을 조회
     * - UserCoupon 기준으로 보유 쿠폰 조회
     * - 쿠폰 정보를 가져와 할인 금액 포함한 응답 생성
     */
    public List<CouponResponse> getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(userId);

        return userCoupons.stream()
            .map(uc -> {
                Coupon coupon = couponRepository.findById(uc.couponId())
                    .orElse(null); // 쿠폰 정보가 없을 경우 할인 금액 0 처리
                return CouponResponse.from(uc, coupon != null ? coupon.getDiscountAmount() : 0);
            })
            .toList();
    }

    /**
     * 쿠폰 발급 요청 처리
     * - 중복 발급 방지
     * - 발급 가능 수량 초과 방지
     * - 발급 후 쿠폰 수량 증가 + 저장
     */
    public CouponResponse issueCoupon(Long userId, Long couponId) {
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
        coupon.issue(); // 발급 수량 증가
        couponRepository.save(coupon); // 변경된 수량 저장

        // 4. 유저-쿠폰 관계 저장
        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        // 5. 응답 반환
        return CouponResponse.from(userCoupon, coupon.getDiscountAmount());
    }

}
