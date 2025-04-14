package kr.hhplus.be.server.application.coupon.usecase;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 사용자 보유 쿠폰 목록 조회 UseCase
 * - 사용자가 발급받은 쿠폰 정보를 조회하고 쿠폰 할인 금액을 함께 반환한다.
 */
@Component
public class GetUserCouponsUseCase {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    public GetUserCouponsUseCase(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    /**
     * 사용자의 보유 쿠폰 목록 조회
     * - UserCoupon 기준으로 Coupon 정보를 병합하여 응답 생성
     */
    public List<CouponResponse> execute(Long userId) {
        List<UserCoupon> userCoupons = userCouponRepository.findAllByUserId(userId);

        return userCoupons.stream()
                .map(uc -> {
                    Coupon coupon = couponRepository.findById(uc.couponId())
                            .orElse(null);
                    return CouponResponse.from(uc, coupon != null ? coupon.getDiscountAmount() : 0);
                })
                .toList();
    }
}

