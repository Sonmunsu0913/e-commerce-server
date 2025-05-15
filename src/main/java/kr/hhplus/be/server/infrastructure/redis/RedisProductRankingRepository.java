package kr.hhplus.be.server.infrastructure.redis;

import java.time.Duration;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.infrastructure.product.repository.ProductRankingRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Redis Sorted Set을 활용한 "일간" 상품 주문 랭킹 저장소 구현체
 * 날짜별 키로 주문 수량을 누적하여 일간 인기 상품 랭킹을 관리
 */
@Repository
public class RedisProductRankingRepository implements ProductRankingRepository {

    private final StringRedisTemplate redisTemplate;

    public RedisProductRankingRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Duration TTL = Duration.ofHours(25); // TTL 반영

    /**
     * 단일 상품의 주문 수량을 해당 일자의 Redis Sorted Set에 반영
     * 내부적으로 ZINCRBY 명령이 실행
     * key는 'product:order:ranking:yyyyMMdd' 형식으로 날짜별로 구분
     */
    @Override
    public void increaseScore(Long productId, int quantity) {
        String key = generateDailyRankingKey();
        redisTemplate.opsForZSet().incrementScore(key, productId.toString(), quantity);
        redisTemplate.expire(key, TTL);
    }

    /**
     * 여러 상품의 주문 수량을 해당 일자의 Sorted Set에 순차적으로 반영
     * 파이프라인은 사용하지 않으며, 각 항목마다 ZINCRBY를 실행
     */
    @Override
    public void increaseScoreBatch(List<OrderItemCommand> items) {
        String key = generateDailyRankingKey();
        for (OrderItemCommand item : items) {
            redisTemplate.opsForZSet().incrementScore(
                key,
                item.productId().toString(),
                item.quantity()
            );
        }
        redisTemplate.expire(key, TTL);
    }

    /**
     * 오늘 날짜 기준 랭킹 키 생성 (예: product:order:ranking:20250513)
     */
    private String generateDailyRankingKey() {
        String today = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.BASIC_ISO_DATE);
        return "product:order:ranking:" + today;
    }


}

