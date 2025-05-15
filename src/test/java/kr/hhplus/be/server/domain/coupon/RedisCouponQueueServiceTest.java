package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.service.RedisCouponQueueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

class RedisCouponQueueServiceTest {

    private StringRedisTemplate redisTemplate;
    private SetOperations<String, String> setOps;
    private ListOperations<String, String> listOps;
    private RedisCouponQueueService redisCouponQueueService;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        setOps = mock(SetOperations.class);
        listOps = mock(ListOperations.class);

        given(redisTemplate.opsForSet()).willReturn(setOps);
        given(redisTemplate.opsForList()).willReturn(listOps);

        redisCouponQueueService = new RedisCouponQueueService(redisTemplate);
    }

    @Test
    void 쿠폰_발급_스크립트_정상_동작_발급성공() {
        Long couponId = 100L;
        Long userId = 1L;

        given(redisTemplate.execute(any(), anyList(), any())).willReturn(1L);

        boolean issued = redisCouponQueueService.tryIssueCoupon(couponId, userId);

        System.out.println("발급 성공 여부: " + issued);
        assertThat(issued).isTrue();
    }

    @Test
    void 중복없이_큐에_추가된다() {
        // given
        Long couponId = 100L;
        Long userId = 1L;

        given(setOps.add(eq("coupon:issued:100"), eq("1"))).willReturn(1L);
        given(listOps.rightPush(eq("coupon:queue:100"), eq("1"))).willReturn(1L);

        // when
        boolean added = redisCouponQueueService.pushToQueue(couponId, userId);

        // then
        System.out.println("✅ 큐 추가 결과: " + added);
        assertThat(added).isTrue();
        then(listOps).should().rightPush("coupon:queue:100", "1");
    }



    @Test
    void 중복이면_큐에_추가되지_않는다() {
        // given
        Long couponId = 100L;
        Long userId = 1L;
        given(setOps.add("coupon:users:100", "1")).willReturn(0L);

        // when
        boolean added = redisCouponQueueService.pushToQueue(couponId, userId);

        // then
        System.out.println("중복 추가 시도 결과: " + added);
        assertThat(added).isFalse();
        then(listOps).should(never()).rightPush(anyString(), anyString());
    }

    @Test
    void 큐에서_유저ID_하나를_꺼낸다() {
        // given
        Long couponId = 100L;
        given(listOps.leftPop("coupon:queue:100")).willReturn("1");

        // when
        String userIdStr = redisCouponQueueService.popFromQueue(couponId);

        // then
        System.out.println("큐에서 꺼낸 userId: " + userIdStr);
        assertThat(userIdStr).isEqualTo("1");
    }
}
