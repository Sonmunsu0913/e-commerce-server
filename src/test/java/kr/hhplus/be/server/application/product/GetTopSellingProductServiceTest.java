package kr.hhplus.be.server.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.service.GetTopSellingProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.product.PopularProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetTopSellingProductServiceTest {

    @Mock
    ProductSaleRepository productSaleRepository;

    @Mock
    ProductRepository productRepository;

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
        List<PopularProductResponse> result = useCase.execute(range);

        // then
        assertEquals(3, result.size());
        assertEquals("순대", result.get(0).getProductName());
    }

    @Test
    void 지원하지_않는_range_형식_예외() {
        // given
        String range = "3w";

        // when & then
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(range));
    }
}
