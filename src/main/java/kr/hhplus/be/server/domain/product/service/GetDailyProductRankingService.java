package kr.hhplus.be.server.domain.product.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.api.product.ProductRankingResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

/**
 * Redis에 저장된 일간 상품 랭킹 조회 서비스
 */
@Service
public class GetDailyProductRankingService {

    private final StringRedisTemplate redisTemplate;
    private final ProductRepository productRepository;

    public GetDailyProductRankingService(StringRedisTemplate redisTemplate, ProductRepository productRepository) {
        this.redisTemplate = redisTemplate;
        this.productRepository = productRepository;
    }

    /**
     * 특정 날짜 기준 인기 상품 랭킹 조회
     * Redis Sorted Set에서 점수 높은 순으로 limit만큼 조회
     */
    public List<ProductRankingResponse> getRanking(LocalDate date, int limit) {
        // Redis 키 생성 (예: product:order:ranking:20250513)
        String key = "product:order:ranking:" + date.format(DateTimeFormatter.BASIC_ISO_DATE);

        // 점수 높은 순으로 조회
        Set<TypedTuple<String>> ranking = redisTemplate.opsForZSet()
            .reverseRangeWithScores(key, 0, limit - 1);

        // 랭킹 비어있으면 빈 리스트 반환
        if (ranking == null || ranking.isEmpty()) {
            return List.of();
        }

        // productId 추출
        List<Long> productIds = ranking.stream()
            .map(tuple -> Long.valueOf(tuple.getValue()))
            .toList();

        // 상품 이름 매핑
        Map<Long, String> productNames = productRepository.findAllByIdIn(productIds).stream()
            .collect(Collectors.toMap(Product::id, Product::name));

        // 응답 객체로 변환
        return ranking.stream()
            .map(tuple -> new ProductRankingResponse(
                Long.valueOf(tuple.getValue()),
                productNames.getOrDefault(Long.valueOf(tuple.getValue()), "알 수 없음"),
                tuple.getScore() != null ? tuple.getScore() : 0.0
            ))
            .toList();
    }
}

