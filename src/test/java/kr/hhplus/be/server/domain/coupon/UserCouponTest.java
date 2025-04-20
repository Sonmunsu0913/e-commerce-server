package kr.hhplus.be.server.domain.coupon;

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

}
