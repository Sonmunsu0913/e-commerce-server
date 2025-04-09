package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProductSaleStatisticsTest {

    @Test
    void 판매_수량_정상_집계() {
        List<ProductSale> sales = List.of(
                new ProductSale(1L, LocalDate.now(), 3),
                new ProductSale(2L, LocalDate.now(), 5),
                new ProductSale(1L, LocalDate.now(), 2)
        );

        Map<Long, Long> result = ProductSaleStatistics.aggregateSales(sales);

        assertEquals(2, result.size());
        assertEquals(5L, result.get(1L));
        assertEquals(5L, result.get(2L));
    }

    @Test
    void 상위_N개_상품_정렬_정상() {
        Map<Long, Long> saleMap = Map.of(
                1L, 50L,
                2L, 100L,
                3L, 30L,
                4L, 70L
        );

        List<Map.Entry<Long, Long>> top2 = ProductSaleStatistics.topN(saleMap, 2);

        assertEquals(2, top2.size());
        assertEquals(2L, top2.get(0).getKey()); // 100
        assertEquals(4L, top2.get(1).getKey()); // 70
    }

    @Test
    void 상위_엔트리로부터_ID_추출() {
        Map<Long, Long> saleMap = Map.of(
                10L, 300L,
                20L, 150L,
                30L, 500L
        );

        List<Map.Entry<Long, Long>> top = ProductSaleStatistics.topN(saleMap, 2);
        List<Long> ids = ProductSaleStatistics.extractProductIds(top);

        assertEquals(List.of(30L, 10L), ids); // 500, 300 순
    }
}
