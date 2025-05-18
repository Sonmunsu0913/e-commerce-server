package kr.hhplus.be.server.domain.coupon.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class RedisCouponQueueService {

    private final StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> issueScript;

    public RedisCouponQueueService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        // Lua 스크립트 정의: 원자적으로 발급 조건 체크 및 처리
        this.issueScript = new DefaultRedisScript<>();
        this.issueScript.setScriptText(
            """
            -- 이미 발급된 유저인지 확인
            if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
              return 0
            end
        
            -- 재고 리스트에서 하나 꺼냄
            local stock = redis.call('LPOP', KEYS[1])
            if stock then
              -- 발급 처리
              redis.call('SADD', KEYS[2], ARGV[1])
              return 1
            else
              return -1
            end
            """
        );
        this.issueScript.setResultType(Long.class);
    }

    /**
     * 선착순 쿠폰 발급 시도
     * - 발급 성공 시 true
     * - 이미 발급받았거나 재고 없음 시 false
     */
    public boolean tryIssueCoupon(Long couponId, Long userId) {
        String stockKey = "coupon:stock:" + couponId;     // 쿠폰 재고 저장 Set (ex. "1", "1", ...)
        String issuedKey = "coupon:issued:" + couponId;   // 발급된 유저 ID Set

        Long result = redisTemplate.execute(
            issueScript,
            Arrays.asList(stockKey, issuedKey),
            String.valueOf(userId)
        );

        if (result == null || result == 0) {
            // 이미 발급받은 경우 또는 예외 상황
            return false;
        } else if (result == 1) {
            // 발급 성공
            return true;
        } else {
            // 재고가 없어서 실패 → 대기열에 유저 추가
            redisTemplate.opsForList().rightPush("coupon:queue:" + couponId, String.valueOf(userId));
            return false;
        }
    }

    /**
     * 쿠폰 대기열에서 한 명을 꺼냄 (스케줄러에서 사용)
     */
    public String popFromQueue(Long couponId) {
        return redisTemplate.opsForList().leftPop("coupon:queue:" + couponId);
    }

    /**
     * 중복 없이 대기열에 유저를 추가
     * - issued Set에 먼저 추가 시도 (중복 제거)
     * - 추가되었으면 대기열(List)에 푸시
     */
    public boolean pushToQueue(Long couponId, Long userId) {
        String issuedKey = "coupon:issued:" + couponId;
        String queueKey = "coupon:queue:" + couponId;

        Long added = redisTemplate.opsForSet().add(issuedKey, String.valueOf(userId));
        if (added != null && added > 0) {
            redisTemplate.opsForList().rightPush(queueKey, String.valueOf(userId));
            return true;
        }
        return false; // 이미 Set에 있음
    }
}
