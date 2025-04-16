package kr.hhplus.be.server.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.service.GetProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    GetProductService useCase;

    @Test
    void 전체_상품_조회() {
        // given
        List<Product> products = List.of(
            new Product(1L, "떡볶이", 5000, 10),
            new Product(2L, "순대", 4000, 5)
        );
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<ProductResponse> result = useCase.execute();

        // then
        assertEquals(2, result.size());
        assertEquals("떡볶이", result.get(0).getName());
        assertEquals("순대", result.get(1).getName());
    }
}

