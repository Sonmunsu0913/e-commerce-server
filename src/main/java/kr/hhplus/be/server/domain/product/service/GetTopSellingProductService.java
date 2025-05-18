package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.ProductSaleStatistics;
import kr.hhplus.be.server.interfaces.api.product.PopularProductResponse;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kr.hhplus.be.server.interfaces.api.product.ProductRankingResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

/**
 * 인기 상품 조회 유스케이스 (최근 n일 기준)
 */
@Service
public class GetTopSellingProductService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;
    private final StringRedisTemplate redisTemplate;

    public GetTopSellingProductService(ProductSaleRepository productSaleRepository,
                                       ProductRepository productRepository,
                                       StringRedisTemplate redisTemplate) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 단순 캐시 처리. 캐시가 없으면 계산해서 저장됨.
     */
    @Cacheable(value = "POPULAR_PRODUCTS", key = "#range")
    public List<PopularProductResponse> getFromDb(String range) {
        System.out.println("캐시 MISS - DB 조회");
        return calculate(range);
    }

    /**
     * pre-warming, 강제 갱신용 캐시 Put
     */
    @CachePut(value = "POPULAR_PRODUCTS", key = "#range")
    public List<PopularProductResponse> refresh(String range) {
        System.out.println("캐시 강제 갱신");
        return calculate(range);
    }

    /**
     * 최근 판매 이력을 기반으로 DB에서 인기 상품 조회
     */
    private List<PopularProductResponse> calculate(String range) {
        int days = parseRangeToDays(range);
        LocalDate from = LocalDate.now().minusDays(days);

        List<ProductSale> sales = productSaleRepository.findSalesAfter(from);
        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);
        List<Map.Entry<Long, Long>> topEntries = statistics.topN(5);

        Map<Long, String> productNames = productRepository.findAllByIdIn(statistics.extractProductIds(topEntries))
            .stream().collect(Collectors.toMap(Product::id, Product::name));

        return topEntries.stream()
            .map(entry -> new PopularProductResponse(
                entry.getKey(),
                productNames.getOrDefault(entry.getKey(), "알 수 없음"),
                entry.getValue()
            )).toList();
    }

    /**
     * Redis ZUNIONSTORE를 이용한 최근 N일간 인기 상품 랭킹 조회
     */
    public List<ProductRankingResponse> getFromRedis(String range) {
        int days = parseRangeToDays(range);
        List<String> keys = generateKeysForLastNDays(days);

        String unionKey = "product:order:ranking:union:" + range;
        redisTemplate.opsForZSet().unionAndStore(keys.get(0), keys.subList(1, keys.size()), unionKey);

        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
            .reverseRangeWithScores(unionKey, 0, 4); // Top 5 고정

        if (tuples == null || tuples.isEmpty()) return List.of();

        List<Long> productIds = tuples.stream().map(t -> Long.valueOf(t.getValue())).toList();
        Map<Long, String> names = productRepository.findAllByIdIn(productIds).stream()
            .collect(Collectors.toMap(Product::id, Product::name));

        return tuples.stream()
            .map(t -> new ProductRankingResponse(
                Long.valueOf(t.getValue()),
                names.getOrDefault(Long.valueOf(t.getValue()), "알 수 없음"),
                t.getScore() != null ? t.getScore() : 0.0
            ))
            .toList();
    }

//    public List<PopularProductResponse> execute(String range) {
//        String key = range;
//        String lockKey = "lock:POPULAR_PRODUCTS:" + range;
//
//        Cache cache = cacheManager.getCache("POPULAR_PRODUCTS");
//        if (cache == null) throw new IllegalStateException("캐시가 존재하지 않습니다.");
//
//        List<PopularProductResponse> cached = cache.get(key, List.class);
//        if (cached != null) {
//            System.out.println("캐시 HIT");
//            return cached;
//        }
//
//        RLock lock = redissonClient.getLock(lockKey);
//        try {
//            // waitTime: 최대 대기 시간, leaseTime: 자동 해제 시간
//            boolean available = lock.tryLock(3, 10, TimeUnit.SECONDS);
//            if (available) {
//                try {
//                    // double-check
//                    cached = cache.get(key, List.class);
//                    if (cached != null) {
//                        System.out.println("캐시 HIT (after lock)");
//                        return cached;
//                    }
//
//                    System.out.println("캐시 MISS - DB 조회");
//                    List<PopularProductResponse> result = calculate(range);
//                    cache.put(key, result);
//                    return result;
//                } finally {
//                    lock.unlock();
//                }
//            } else {
//                throw new IllegalStateException("분산락 획득 실패");
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new IllegalStateException("락 대기 중 인터럽트 발생", e);
//        }
//    }

    // 테스트용 - 캐시와 락 모두 제거한 로직
    public List<PopularProductResponse> executeWithoutCache(String range) {
        return calculateWithoutCache(range);
    }

    // 테스트용 - 캐시와 락 모두 제거한 로직
    private List<PopularProductResponse> calculateWithoutCache(String range) {
        int days = parseRangeToDays(range);
        LocalDate from = LocalDate.now().minusDays(days);

        // 1. 최근 판매 내역 조회
        List<ProductSale> sales = productSaleRepository.findSalesAfter(from);

        // 2. 판매 통계 집계
        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);
        List<Map.Entry<Long, Long>> topEntries = statistics.topN(5);

        // 3. 상품 이름 조회
        Map<Long, String> productNames = productRepository.findAllByIdIn(statistics.extractProductIds(topEntries))
                .stream().collect(Collectors.toMap(Product::id, Product::name));

        // 4. 응답 객체로 변환
        return topEntries.stream()
                .map(entry -> new PopularProductResponse(
                        entry.getKey(),
                        productNames.getOrDefault(entry.getKey(), "알 수 없음"),
                        entry.getValue()
                )).toList();
    }

    // "d" → int 로 변환
    private int parseRangeToDays(String range) {
        if (range.endsWith("d")) {
            try {
                return Integer.parseInt(range.replace("d", ""));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("range 형식이 잘못되었습니다. 예: 3d");
            }
        }
        throw new IllegalArgumentException("지원하지 않는 range 단위입니다. 예: 3d");
    }

    /**
     * 최근 N일간의 Redis 랭킹 키 생성
     */
    private List<String> generateKeysForLastNDays(int days) {
        LocalDate now = LocalDate.now();
        return IntStream.rangeClosed(0, days - 1)
            .mapToObj(i -> now.minusDays(i).format(DateTimeFormatter.BASIC_ISO_DATE))
            .map(date -> "product:order:ranking:" + date)
            .toList();
    }
}