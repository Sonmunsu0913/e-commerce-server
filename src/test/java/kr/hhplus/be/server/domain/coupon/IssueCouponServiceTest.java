package kr.hhplus.be.server.domain.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
        when(couponRepository.findWithPessimisticLockById(1L)).thenReturn(coupon);

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

        when(couponRepository.findWithPessimisticLockById(1L)).thenReturn(coupon);
        when(userCouponRepository.existsByUserIdAndCouponId(userId, 1L)).thenReturn(true); // 이미 보유 상태로 설정

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId));
    }

    @Test
    void 발급가능한_쿠폰_없으면_예외() {
        // given
        Long userId = 1L;
        Coupon exhausted = new Coupon(1L, "마감쿠폰", 1000, 1);
        exhausted.issue();

        when(couponRepository.findAllCoupons()).thenReturn(List.of(exhausted));
        lenient().when(couponRepository.findWithPessimisticLockById(1L)).thenReturn(exhausted); // ← lenient 적용

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId));
    }
}

