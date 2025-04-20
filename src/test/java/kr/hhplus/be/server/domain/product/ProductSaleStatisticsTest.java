package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.jupiter.api.Assertions.*;

class ProductSaleStatisticsTest {

    @Test
    void 판매_수량_정상_집계() {
        List<ProductSale> sales = List.of(
            new ProductSale(1L, LocalDate.now(), 3),
            new ProductSale(2L, LocalDate.now(), 5),
            new ProductSale(1L, LocalDate.now(), 2)
        );

        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);
        Map<Long, Long> result = statistics.getTotalSalesPerProduct();

        assertEquals(2, result.size());
        assertEquals(5L, result.get(1L)); // 3 + 2
        assertEquals(5L, result.get(2L));
    }

    @Test
    void 상위_N개_상품_정렬_정상() {
        List<ProductSale> sales = List.of(
            new ProductSale(1L, LocalDate.now(), 50),
            new ProductSale(2L, LocalDate.now(), 100),
            new ProductSale(3L, LocalDate.now(), 30),
            new ProductSale(4L, LocalDate.now(), 70)
        );

        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);

        List<Map.Entry<Long, Long>> top2 = statistics.topN(2);

        assertEquals(2, top2.size());
        assertEquals(2L, top2.get(0).getKey()); // 100
        assertEquals(4L, top2.get(1).getKey()); // 70
    }


    @Test
    void 상위_엔트리로부터_ID_추출() {
        List<ProductSale> sales = List.of(
            new ProductSale(10L, LocalDate.now(), 300),
            new ProductSale(20L, LocalDate.now(), 150),
            new ProductSale(30L, LocalDate.now(), 500)
        );

        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);

        List<Entry<Long, Long>> top = statistics.topN(2);
        List<Long> ids = statistics.extractProductIds(top);

        assertEquals(List.of(30L, 10L), ids); // 500, 300 순
    }

    @Test
    void 집계_데이터가_없는_경우_처리() {
        ProductSaleStatistics statistics = ProductSaleStatistics.of(List.of());

        Map<Long, Long> result = statistics.getTotalSalesPerProduct();
        assertTrue(result.isEmpty());
    }

}
