package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.service.ProductSaleService;
import kr.hhplus.be.server.application.product.service.ProductSaleStatisticsService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSaleStatisticsServiceTest {

    @Mock
    private ProductSaleService productSaleService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductSaleStatisticsService productSaleStatisticsService;

    @Test
    void 최근_3일간_인기상품_상위5개_정렬_정상조회() {
        // given
        LocalDate today = LocalDate.now();
        LocalDate from = today.minusDays(3);

        List<ProductSale> mockSales = List.of(
            new ProductSale(1L, today.minusDays(1), 10),
            new ProductSale(2L, today.minusDays(2), 5),
            new ProductSale(3L, today.minusDays(1), 20),
            new ProductSale(4L, today.minusDays(1), 15),
            new ProductSale(5L, today.minusDays(2), 8),
            new ProductSale(6L, today.minusDays(1), 3),
            new ProductSale(7L, today.minusDays(4), 100)
        );

        when(productSaleService.findSalesAfter(eq(from))).thenReturn(
            mockSales.stream().filter(sale -> !sale.saleDate().isBefore(from)).toList()
        );

        when(productRepository.findAllByIdIn(any())).thenReturn(List.of(
            new Product(1L, "콜라", 1500, 10),
            new Product(2L, "사이다", 1400, 5),
            new Product(3L, "환타", 1300, 7),
            new Product(4L, "스프라이트", 1600, 4),
            new Product(5L, "포카리", 1700, 6),
            new Product(6L, "게토레이", 1100, 12)
        ));

        // when
        List<PopularProductResponse> result = productSaleStatisticsService.getTopSellingProducts("3d");

        // then
        assertEquals(5, result.size());

        // 판매량 기준 정렬: 20 → 15 → 10 → 8 → 5
        assertEquals("환타", result.get(0).getProductName());         // 20
        assertEquals("스프라이트", result.get(1).getProductName());     // 15
        assertEquals("콜라", result.get(2).getProductName());         // 10
        assertEquals("포카리", result.get(3).getProductName());       // 8
        assertEquals("사이다", result.get(4).getProductName());       // 5
    }
}
