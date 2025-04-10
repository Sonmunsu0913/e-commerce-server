package kr.hhplus.be.server.application.coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.coupon.dto.CouponResponse;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public CouponService(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public List<CouponResponse> getUserCoupons(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(userId);

        return userCoupons.stream()
            .map(uc -> new CouponResponse(
                uc.getCouponId(),
                couponRepository.findById(uc.getCouponId())
                    .map(Coupon::getDiscountAmount)
                    .orElse(0),
                uc.getClaimedAt(),
                uc.isUsed()
            ))
            .toList();
    }

    public CouponResponse issueCoupon(Long userId, Long couponId) {
        // 1. 쿠폰 조회
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        // 2. 중복 발급 여부 확인
        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        // 3. 선착순 수량 확인 및 발급 처리
        coupon.issue(); // 내부에서 수량 검사 및 증가 처리

        // 4. 사용자에게 발급 정보 저장
        UserCoupon userCoupon = new UserCoupon(
            couponId,
            userId,
            false,
            LocalDateTime.now().toString()
        );
        userCouponRepository.save(userCoupon);

        // 5. 응답 생성
        return new CouponResponse(
            couponId,
            coupon.getDiscountAmount(),
            userCoupon.getClaimedAt(),
            userCoupon.isUsed()
        );
    }
}
