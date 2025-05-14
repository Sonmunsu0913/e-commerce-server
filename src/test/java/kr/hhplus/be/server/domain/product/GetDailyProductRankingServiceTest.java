package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.service.GetDailyProductRankingService;
import kr.hhplus.be.server.interfaces.api.product.ProductRankingResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GetDailyProductRankingServiceTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GetDailyProductRankingService getDailyProductRankingService;

    private String key;
    private LocalDate today;

    @BeforeEach
    void setup() {
        today = LocalDate.now();
        key = "product:order:ranking:" + today.format(DateTimeFormatter.BASIC_ISO_DATE);
        redisTemplate.delete(key); // 기존 ZSet 삭제

        // 상품 저장
        productRepository.save(new Product(1L, "상품1", 1000, 100));
        productRepository.save(new Product(2L, "상품2", 2000, 100));
        productRepository.save(new Product(3L, "상품3", 3000, 100));

        // Redis ZSet 점수 추가
        redisTemplate.opsForZSet().add(key, "1", 5);
        redisTemplate.opsForZSet().add(key, "2", 10);
        redisTemplate.opsForZSet().add(key, "3", 7);
    }

    @Test
    void Redis_랭킹_조회_정상작동() {
        List<ProductRankingResponse> result = getDailyProductRankingService.getRanking(today, 3);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).productId()).isEqualTo(2L); // 10점
        assertThat(result.get(1).productId()).isEqualTo(3L); // 7점
        assertThat(result.get(2).productId()).isEqualTo(1L); // 5점

        System.out.println("📊 조회 결과:");
        result.forEach(r -> System.out.println(r.productId() + " / " + r.name() + " / " + r.score()));
    }

    @Test
    void 랭킹_데이터가_없는_날짜는_빈_리스트_반환() {
        LocalDate futureDate = LocalDate.now().plusDays(7); // 향후 7일 뒤
        List<ProductRankingResponse> result = getDailyProductRankingService.getRanking(futureDate, 5);

        assertThat(result).isEmpty();
        System.out.println(futureDate + " 기준 랭킹 없음 → 반환된 리스트 크기: " + result.size());
    }

    @Test
    void 날짜별_랭킹_저장_정상작동() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        String yesterdayKey = "product:order:ranking:" + yesterday.format(DateTimeFormatter.BASIC_ISO_DATE);

        // Redis 키 초기화 (다른 테스트 영향 방지)
        redisTemplate.delete(yesterdayKey);

        // 상품 정보 저장
        Product product = productRepository.save(new Product(null, "상품1", 1000, 50));

        // 어제 날짜 랭킹 데이터 추가
        redisTemplate.opsForZSet().add(yesterdayKey, String.valueOf(product.id()), 50);

        // when
        List<ProductRankingResponse> yesterdayRanking = getDailyProductRankingService.getRanking(yesterday, 3);

        // then
        System.out.println("✅ 어제 랭킹 확인:");
        yesterdayRanking.forEach(r -> System.out.println(r.productId() + " / " + r.name() + " / " + r.score()));

        assertThat(yesterdayRanking).hasSize(1);
        assertThat(yesterdayRanking.get(0).productId()).isEqualTo(product.id());
        assertThat(yesterdayRanking.get(0).name()).isEqualTo("상품1");
        assertThat(yesterdayRanking.get(0).score()).isEqualTo(50.0);
    }

}


