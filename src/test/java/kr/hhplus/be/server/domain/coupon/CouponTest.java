package kr.hhplus.be.server.domain.coupon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    @Test
    void 발급가능하면_발급수량이_증가() {
        // given
        Coupon coupon = new Coupon(101L, "테스트 쿠폰", 1000, 3);

        // when
        coupon.issue();

        // then
        assertEquals(1, coupon.getIssuedCount());
        assertTrue(coupon.canIssue());
    }

    @Test
    void 발급수량을_초과하면_예외가_발생() {
        // given
        Coupon coupon = new Coupon(102L, "제한 쿠폰", 1500, 2);

        // when
        coupon.issue();
        coupon.issue();

        // then
        IllegalStateException ex = assertThrows(IllegalStateException.class, coupon::issue);
        assertEquals("발급 가능한 쿠폰이 없습니다.", ex.getMessage());
    }

    @Test
    void 발급수량과_총수량이_같으면_canIssue는_false를_반환() {
        // given
        Coupon coupon = new Coupon(103L, "한정 쿠폰", 2000, 1);
        coupon.issue();

        // when
        boolean canIssue = coupon.canIssue();

        // then
        assertFalse(canIssue);
    }
}
