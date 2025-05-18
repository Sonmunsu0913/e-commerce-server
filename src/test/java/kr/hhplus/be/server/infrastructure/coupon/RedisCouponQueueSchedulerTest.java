package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.service.RedisCouponQueueService;
import kr.hhplus.be.server.infrastructure.scheduler.RedisCouponQueueScheduler;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class RedisCouponQueueSchedulerTest {

    @Mock
    RedisCouponQueueService redisCouponQueueService;

    @InjectMocks
    RedisCouponQueueScheduler scheduler;

    @Test
    void 대기열_쿠폰_발급_정상동작() {
        Long couponId = 100L;

        // given
        when(redisCouponQueueService.popFromQueue(couponId))
            .thenReturn("1", "2", "3", "4", "5");  // 최대 5번만 처리됨

        when(redisCouponQueueService.tryIssueCoupon(eq(couponId), anyLong()))
            .thenReturn(true);  // 모두 발급 성공

        // when
        scheduler.processCouponQueue();

        // then
        // 5명만 popFromQueue() 호출되므로 검증도 5회로 제한
        verify(redisCouponQueueService, times(5)).popFromQueue(couponId);
        verify(redisCouponQueueService, times(5)).tryIssueCoupon(eq(couponId), anyLong());
    }


    @Test
    void 대기열에_유저가_없는경우_바로종료() {
        Long couponId = 100L;

        // given
        when(redisCouponQueueService.popFromQueue(couponId))
            .thenReturn(null);  // 첫 시도에 종료

        // when
        scheduler.processCouponQueue();

        // then
        verify(redisCouponQueueService, times(1)).popFromQueue(couponId);
        verify(redisCouponQueueService, never()).tryIssueCoupon(anyLong(), anyLong());
    }
}
