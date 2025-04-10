package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    @Test
    void 상품_목록_조회() {
        // given
        Product product = new Product(1L, "콜라", 1500, 10);
        when(productRepository.findAll()).thenReturn(java.util.List.of(product));

        // when
        var products = productService.getAllProducts();

        // then
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("콜라", products.get(0).getName());
    }
}

