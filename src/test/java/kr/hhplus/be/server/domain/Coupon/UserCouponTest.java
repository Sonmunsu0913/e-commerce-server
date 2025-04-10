package kr.hhplus.be.server.domain.Coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserCouponTest {

    @Test
    void 팩토리_메서드로_정상_생성() {
        // given
        Long userId = 1L;
        Long couponId = 101L;

        // when
        UserCoupon coupon = UserCoupon.create(userId, couponId);

        // then
        assertEquals(userId, coupon.getUserId());
        assertEquals(couponId, coupon.getCouponId());
        assertFalse(coupon.isUsed());
        assertNotNull(coupon.getIssuedAt());
    }

    @Test
    void 생성자_직접호출도_정상_작동() {
        // given
        UserCoupon coupon = new UserCoupon(101L, 2L, true, "2025-04-10T18:00:00");

        // then
        assertEquals(101L, coupon.getCouponId());
        assertEquals(2L, coupon.getUserId());
        assertTrue(coupon.isUsed());
        assertEquals("2025-04-10T18:00:00", coupon.getIssuedAt());
    }
}
