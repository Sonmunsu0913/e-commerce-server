package kr.hhplus.be.server.domain.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IssueCouponServiceTest {

    @Mock
    UserCouponRepository userCouponRepository;

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    IssueCouponService useCase;

    @Test
    void 쿠폰_정상_발급() {
        // given
        Long userId = 1L;
        Coupon coupon = new Coupon(1L, "할인", 2000, 10);   // 재고 10
        when(couponRepository.findAllCoupons())
                .thenReturn(List.of(coupon));              // ← “임시 쿠폰” 주입
        when(userCouponRepository.existsByUserIdAndCouponId(userId, 1L))
                .thenReturn(false);

        // when
        CouponResponse res = useCase.execute(userId);

        // then
        assertEquals(1L, res.couponId());
        assertEquals(2000, res.discountAmount());
        verify(couponRepository).save(coupon);             // issue() 후 save 호출
        verify(userCouponRepository).save(any());
    }

    @Test
    void 중복_쿠폰_발급시_예외() {
        // given
        Long userId   = 1L;
        Coupon coupon = new Coupon(1L, "중복쿠폰", 1000, 10);

        when(couponRepository.findAllCoupons())
                .thenReturn(List.of(coupon));               // 쿠폰 한 장만 존재
        when(userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId()))
                .thenReturn(true);                           // 이미 발급된 상태

        // then ‑ 이미 보유 → 예외
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId));
    }

    @Test
    void 발급가능한_쿠폰_없으면_예외() {
        // given
        Long userId = 1L;
        Coupon exhausted = new Coupon(1L, "마감쿠폰", 1000, 1);
        exhausted.issue();                       // canIssue() == false

        when(couponRepository.findAllCoupons())
                .thenReturn(List.of(exhausted)); // 재고 0 쿠폰만 존재

        // then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId));
    }
}

