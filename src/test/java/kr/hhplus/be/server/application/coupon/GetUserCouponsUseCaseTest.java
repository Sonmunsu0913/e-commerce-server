package kr.hhplus.be.server.application.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.usecase.GetUserCouponsUseCase;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserCouponsUseCaseTest {

    @Mock
    UserCouponRepository userCouponRepository;

    @Mock
    CouponRepository couponRepository;

    @InjectMocks
    GetUserCouponsUseCase useCase;

    @Test
    void 보유_쿠폰_조회_정상() {
        Long userId = 1L;
        UserCoupon userCoupon = UserCoupon.create(userId, 100L);
        Coupon coupon = new Coupon(100L, "할인", 2000, 10);

        when(userCouponRepository.findAllByUserId(userId)).thenReturn(List.of(userCoupon));
        when(couponRepository.findById(100L)).thenReturn(Optional.of(coupon));

        List<CouponResponse> result = useCase.execute(userId);

        assertEquals(1, result.size());
        assertEquals(2000, result.get(0).discountAmount());
    }
}

