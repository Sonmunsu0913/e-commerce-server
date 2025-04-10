package kr.hhplus.be.server.application.coupon.service;

import kr.hhplus.be.server.application.coupon.repository.CouponItemRepository;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponItem;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;
    private final CouponItemRepository couponItemRepository;


    public CouponService(CouponRepository couponRepository,
        UserCouponRepository userCouponRepository,
        CouponItemRepository couponItemRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
        this.couponItemRepository = couponItemRepository;
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

        // 2. 발급 가능한 쿠폰이 남아 있는지 확인
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        // 발급 가능한 쿠폰 중 하나를 찾음
        CouponItem couponItem = couponItemRepository.findFirstByCouponTypeAndIssuedFalse(couponId)
            .orElseThrow(() -> new IllegalStateException("발급 가능한 쿠폰이 없습니다."));

        // 3. 발급
        couponItem.markIssued(); // 쿠폰을 발급 상태로 변경
        couponItemRepository.save(couponItem);  // 발급된 쿠폰 상태 저장

        UserCoupon userCoupon = UserCoupon.create(userId, couponId);  // 유저와 쿠폰 정보 저장
        userCouponRepository.save(userCoupon);

        // 4. 쿠폰 발급 완료 후 응답 반환
        return CouponResponse.from(userCoupon, coupon.getDiscountAmount());
    }
}
