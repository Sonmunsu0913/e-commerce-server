package kr.hhplus.be.server.domain.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import kr.hhplus.be.server.domain.product.service.GetTopSellingProductService;
import kr.hhplus.be.server.interfaces.api.product.PopularProductResponse;
import kr.hhplus.be.server.interfaces.api.product.ProductRankingResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

@ExtendWith(MockitoExtension.class)
class GetTopSellingProductServiceTest {

    @Mock
    ProductSaleRepository productSaleRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    StringRedisTemplate redisTemplate;

    @InjectMocks
    GetTopSellingProductService useCase;

    @Test
    void 인기상품_정상조회() {
        // given
        String range = "3d";
        LocalDate now = LocalDate.now();
        List<ProductSale> sales = List.of(
            new ProductSale(1L, now.minusDays(1), 50),
            new ProductSale(2L, now.minusDays(2), 100),
            new ProductSale(3L, now.minusDays(3), 30)
        );
        when(productSaleRepository.findSalesAfter(now.minusDays(3))).thenReturn(sales);
        when(productRepository.findAllByIdIn(any())).thenReturn(List.of(
            new Product(1L, "떡볶이", 5000, 0),
            new Product(2L, "순대", 4000, 0),
            new Product(3L, "김밥", 3000, 0)
        ));

        // when
        List<PopularProductResponse> result = useCase.getFromDb(range);

        // then
        assertEquals(3, result.size());
        assertEquals("순대", result.get(0).getProductName());
    }

    @Test
    void 지원하지_않는_range_형식_예외() {
        // given
        String range = "3w";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> useCase.getFromDb(range));
    }

    @Test
    void Redis_기반_인기상품_정상조회() {
        // given
        String range = "3d";
        ZSetOperations<String, String> zSetOps = mock(ZSetOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOps);

        Set<ZSetOperations.TypedTuple<String>> redisResult = new LinkedHashSet<>();
        redisResult.add(new DefaultTypedTuple<>("1", 15.0));
        redisResult.add(new DefaultTypedTuple<>("2", 12.0));

        when(zSetOps.reverseRangeWithScores(anyString(), eq(0L), eq(4L))).thenReturn(redisResult);

        when(productRepository.findAllByIdIn(List.of(1L, 2L))).thenReturn(List.of(
            new Product(1L, "라면", 1000, 0),
            new Product(2L, "우동", 1500, 0)
        ));

        // when
        List<ProductRankingResponse> result = useCase.getFromRedis(range);

        // then
        System.out.println("🔍 Redis 조회 결과:");
        for (ProductRankingResponse product : result) {
            System.out.println("상품 ID = " + product.productId() +
                ", 이름 = " + product.name() +
                ", 점수 = " + product.score());
        }

        assertEquals(2, result.size());
        assertEquals("라면", result.get(0).name()); // 점수 15.0
        assertEquals("우동", result.get(1).name()); // 점수 12.0
    }

}
