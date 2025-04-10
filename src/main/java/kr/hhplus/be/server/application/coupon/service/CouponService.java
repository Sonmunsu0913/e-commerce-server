package kr.hhplus.be.server.application.coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
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
                uc.getIssuedAt(),
                uc.isUsed()
            ))
            .toList();
    }

    public CouponResponse issueCoupon(Long userId, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));

        if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
            throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
        }

        coupon.checkAndIncreaseIssuedCount();

        UserCoupon userCoupon = UserCoupon.create(userId, couponId);
        userCouponRepository.save(userCoupon);

        return new CouponResponse(
                couponId,
                coupon.getDiscountAmount(),
                userCoupon.getIssuedAt(),
                userCoupon.isUsed()
        );
    }
}
