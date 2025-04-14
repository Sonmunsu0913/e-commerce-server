package kr.hhplus.be.server.application.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.application.product.usecase.RecordProductSaleUseCase;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordProductSaleUseCaseTest {

    @Mock
    ProductSaleRepository productSaleRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    RecordProductSaleUseCase useCase;

    @Test
    void 판매기록_저장과_재고_차감_정상() {
        // given
        Long productId = 1L;
        Product product = new Product(productId, "떡볶이", 5000, 10);
        ProductSale sale = new ProductSale(productId, LocalDate.now(), 3);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        useCase.execute(sale);

        // then
        assertEquals(7, product.stock());
        verify(productSaleRepository).save(sale);
        verify(productRepository).save(product);
    }

    @Test
    void 상품이_없으면_예외() {
        // given
        Long productId = 999L;
        ProductSale sale = new ProductSale(productId, LocalDate.now(), 1);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(sale));
    }
}

