package kr.hhplus.be.server.domain.Coupon;

import kr.hhplus.be.server.domain.coupon.CouponItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponItemTest {

    @Test
    void 고유_ID_생성_확인() {
        // given
        Long couponTypeId = 101L;
        CouponItem coupon1 = new CouponItem(couponTypeId);
        CouponItem coupon2 = new CouponItem(couponTypeId);

        // when
        Long coupon1Id = coupon1.getId();
        Long coupon2Id = coupon2.getId();

        // then
        assertNotNull(coupon1Id); // ID가 생성되었는지 확인
        assertNotNull(coupon2Id);
        assertNotEquals(coupon1Id, coupon2Id);  // 각 쿠폰은 고유한 ID를 가져야 한다
    }

    @Test
    void 발급_상태_변경_확인() {
        // given
        Long couponTypeId = 101L;
        CouponItem coupon = new CouponItem(couponTypeId);

        // when
        coupon.markIssued();  // 쿠폰 발급 처리

        // then
        assertFalse(coupon.isAvailable());  // 발급된 쿠폰은 available(false)

        // 두 번째 호출 시 예외 발생
        IllegalStateException exception = assertThrows(IllegalStateException.class, coupon::markIssued);
        assertEquals("이미 발급된 쿠폰입니다.", exception.getMessage());  // 예외 메시지 확인
    }

    @Test
    void 이미_발급된_쿠폰은_재발급_불가() {
        // given
        Long couponTypeId = 101L;
        CouponItem coupon = new CouponItem(couponTypeId);

        // when
        coupon.markIssued();  // 첫 번째 발급

        // then
        IllegalStateException exception = assertThrows(IllegalStateException.class,
            coupon::markIssued);
        assertEquals("이미 발급된 쿠폰입니다.", exception.getMessage());
    }
}