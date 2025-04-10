package kr.hhplus.be.server.domain.Coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

    @Test
    void create_메서드로_정상_생성() {
        // given
        Long userId = 1L;
        Long couponId = 101L;

        // when
        UserCoupon coupon = UserCoupon.create(userId, couponId);

        // then
        assertEquals(userId, coupon.userId());
        assertEquals(couponId, coupon.couponId());
        assertFalse(coupon.isUsed());
        assertNotNull(coupon.issuedAt());
    }

    @Test
    void 동일_유저_쿠폰_중복_발급_불가() {
        // given
        Long userId = 1L;
        Long couponId = 101L;

        // when
        UserCoupon coupon1 = UserCoupon.create(userId, couponId);  // 첫 번째 발급
        UserCoupon coupon2 = UserCoupon.create(userId, couponId);  // 두 번째 발급 시도

        // then
        assertNotEquals(coupon1, coupon2);  // 두 객체는 같지 않아야 함 (유저는 하나의 쿠폰만 가질 수 있음)
    }
}
