package kr.hhplus.be.server.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisCacheCheckTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void 캐시_확인() {
        // ⚠이 테스트는 캐시가 먼저 생성되어 있어야 동작함.
        // /api/product/sale/statistics/popular?range=3d API를 먼저 호출해서 캐시를 만든 후 실행해야함.

        String key = "POPULAR_PRODUCTS::3d";

        Boolean exists = redisTemplate.hasKey(key);
        Object value = redisTemplate.opsForValue().get(key);
        Long ttl = redisTemplate.getExpire(key);

        System.out.println("캐시 존재 여부: " + exists);
        System.out.println("캐시 TTL: " + ttl + "초");
        System.out.println("캐시 값: " + value);
    }
}
