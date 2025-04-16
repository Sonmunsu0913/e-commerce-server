package kr.hhplus.be.server.application.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IssueCouponUseCaseTest {

    @Mock
    UserCouponRepository userCouponRepository;

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    IssueCouponUseCase useCase;

    @Test
    void 쿠폰_정상_발급() {
        Long userId = 1L;
        Long couponId = 100L;
        Coupon coupon = new Coupon(couponId, "할인", 2000, 10);

        when(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(false);
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon));

        CouponResponse result = useCase.execute(userId, couponId);

        assertEquals(couponId, result.couponId());
        assertEquals(2000, result.discountAmount());
        verify(userCouponRepository).save(any());
    }

    @Test
    void 중복_쿠폰_발급시_예외() {
        when(userCouponRepository.existsByUserIdAndCouponId(anyLong(), anyLong())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> useCase.execute(1L, 1L));
    }

    @Test
    void 발급가능한_쿠폰_없으면_예외() {
        Coupon exhausted = new Coupon(1L, "마감쿠폰", 1000, 1);
        exhausted.issue(); // count = total

        when(userCouponRepository.existsByUserIdAndCouponId(anyLong(), anyLong())).thenReturn(false);
        when(couponRepository.findById(anyLong())).thenReturn(Optional.of(exhausted));

        assertThrows(IllegalStateException.class, () -> useCase.execute(1L, 1L));
    }
}

