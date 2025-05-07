package kr.hhplus.be.server.infrastructure;


import kr.hhplus.be.server.infrastructure.scheduler.PopularProductCacheScheduler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PopularProductCacheSchedulerTest {

    @Autowired
    private PopularProductCacheScheduler scheduler;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    void 인기상품_스케줄러_캐시_테스트() {
        // when
        scheduler.refreshManually();

        // then
        assertThat(redisTemplate.hasKey("POPULAR_PRODUCTS::3d")).isTrue();

        System.out.println("✅ 3d 캐시: " + redisTemplate.opsForValue().get("POPULAR_PRODUCTS::3d"));
    }
}
