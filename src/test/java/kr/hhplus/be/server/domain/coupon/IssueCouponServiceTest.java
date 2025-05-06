package kr.hhplus.be.server.domain.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.infrastructure.redis.RedisLockRepository;
import kr.hhplus.be.server.interfaces.api.coupon.CouponResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.redisson.api.RedissonClient;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IssueCouponServiceTest {

    @Mock
    UserCouponRepository userCouponRepository;

    @Mock
    CouponRepository couponRepository;

    @Mock
    RedissonClient redissonClient;

    @Mock
    RedisLockRepository redisLockRepository;

    @InjectMocks
    IssueCouponService useCase;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(useCase, "self", useCase);
    }

    @Test
    void 쿠폰_정상_발급() {
        // given
        Long userId = 1L;
        Long couponId = 1L;

        Coupon coupon = new Coupon(couponId, "할인", 2000, 10); // 재고 10
        when(redisLockRepository.tryLockWithRetry(any(), anyInt(), anyLong())).thenReturn(true); // 락 획득 성공
        when(couponRepository.findById(couponId)).thenReturn(Optional.of(coupon)); // 쿠폰 존재
        when(userCouponRepository.findAllByUserId(userId)).thenReturn(List.of()); // 미사용 쿠폰 없음
        when(userCouponRepository.existsByUserIdAndCouponId(userId, couponId)).thenReturn(false); // 중복 발급 아님

        // when
        CouponResponse res = useCase.execute(userId, couponId);

        // then
        assertEquals(couponId, res.couponId());
        assertEquals(2000, res.discountAmount());
        verify(couponRepository).save(coupon); // 수량 감소된 쿠폰 저장
        verify(userCouponRepository).save(any(UserCoupon.class)); // 유저 쿠폰 저장
        verify(redisLockRepository).unlock("coupon:lock:" + couponId); // 락 해제 확인
    }


    @Test
    void 중복_쿠폰_발급시_예외() {
        // given
        Long userId   = 1L;
        Coupon coupon = new Coupon(1L, "중복쿠폰", 1000, 10);

        when(couponRepository.findWithPessimisticLockById(1L)).thenReturn(coupon);
        when(userCouponRepository.existsByUserIdAndCouponId(userId, 1L)).thenReturn(true); // 이미 보유 상태로 설정

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId, 1L));
    }

    @Test
    void 발급가능한_쿠폰_없으면_예외() {
        // given
        Long userId = 1L;
        Coupon exhausted = new Coupon(1L, "마감쿠폰", 1000, 1);
        exhausted.issue();

        when(couponRepository.findAllCoupon()).thenReturn(List.of(exhausted));
        lenient().when(couponRepository.findWithPessimisticLockById(1L)).thenReturn(exhausted); // ← lenient 적용

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId, 1L));
    }
}

