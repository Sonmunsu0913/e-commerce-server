package kr.hhplus.be.server.infrastructure.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Component
public class RedisLockRepository {

    private static final long LOCK_EXPIRE_MILLIS = 3000L;
    private final StringRedisTemplate redisTemplate;

    // 각 스레드마다 고유한 UUID를 저장해서 소유자 검증에 사용
    private final ThreadLocal<String> lockIdHolder = new ThreadLocal<>();

    public RedisLockRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(String key) {
        String uniqueId = UUID.randomUUID().toString();
        Boolean success = redisTemplate.opsForValue()
            .setIfAbsent(key, uniqueId, Duration.ofMillis(LOCK_EXPIRE_MILLIS));
        if (Boolean.TRUE.equals(success)) {
            lockIdHolder.set(uniqueId); // 락 획득 성공 시 고유 ID 저장
            return true;
        }
        return false;
    }

    public boolean unlock(String key) {
        String uniqueId = lockIdHolder.get();
        if (uniqueId == null) return false;

        String script =
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                "  return redis.call('del', KEYS[1]) " +
                "else return 0 end";

        RedisScript<Long> redisScript = RedisScript.of(script, Long.class);
        Long result = redisTemplate.execute(redisScript, List.of(key), uniqueId);

        lockIdHolder.remove(); // ThreadLocal 제거 (메모리 누수 방지)
        return result != null && result == 1;
    }

    public boolean tryLockWithRetry(String key, int retryCount, long retryDelayMillis) {
        for (int i = 0; i < retryCount; i++) {
            if (lock(key)) return true;

            try {
                Thread.sleep(retryDelayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}

