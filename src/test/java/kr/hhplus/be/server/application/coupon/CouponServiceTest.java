package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.application.coupon.repository.CouponItemRepository;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponItem;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class CouponServiceTest {

    private final UserCouponRepository userCouponRepository = Mockito.mock(UserCouponRepository.class);
    private final CouponRepository couponRepository = Mockito.mock(CouponRepository.class);
    private final CouponItemRepository couponItemRepository = Mockito.mock(CouponItemRepository.class);

    private final CouponService couponService = new CouponService(couponRepository, userCouponRepository, couponItemRepository);

    @Test
    void 동일_유저_중복_발급_시_예외_발생() {
        // given
        Long userId = 1L;
        Long couponId = 101L;
        Coupon coupon = new Coupon(couponId, "5천원 할인", 5000, 10);

        when(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(true);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            couponService.issueCoupon(userId, couponId);
        });

        assertEquals("이미 발급받은 쿠폰입니다.", exception.getMessage());
    }

    @Test
    void 쿠폰_발급_가능_시_정상_발급() {
        // given
        Long userId = 1L;
        Long couponId = 101L;
        Coupon coupon = new Coupon(couponId, "5천원 할인", 5000, 10);
        CouponItem couponItem = new CouponItem(couponId);

        when(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(false);
        when(couponRepository.findById(couponId)).thenReturn(java.util.Optional.of(coupon));
        when(couponItemRepository.findFirstByCouponTypeAndIssuedFalse(couponId)).thenReturn(java.util.Optional.of(couponItem));

        // when
        CouponResponse response = couponService.issueCoupon(userId, couponId);

        // then
        assertNotNull(response);
        assertEquals("5천원 할인 쿠폰", response.getName());
    }
}
