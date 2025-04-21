package kr.hhplus.be.server.domain.product;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import kr.hhplus.be.server.domain.product.service.RecordProductSaleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordProductSaleServiceTest {

    @Mock
    ProductSaleRepository productSaleRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    RecordProductSaleService useCase;

    @Test
    void 판매기록_저장과_재고_차감_정상() {
        // given
        Long productId = 1L;
        Product product = new Product(productId, "떡볶이", 5000, 10);
        ProductSale sale = new ProductSale(productId, LocalDate.now(), 3);

        // 비관적 락 메서드에 대해 스텁 설정
        when(productRepository.findWithPessimisticLockById(productId)).thenReturn(product);

        // when
        useCase.execute(sale);

        // then
        verify(productSaleRepository).save(sale);

        // Product 객체가 새로 생성되므로 save()된 객체를 캡처해서 검사
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product savedProduct = captor.getValue();
        assertThat(savedProduct.stock()).isEqualTo(7);  // 10 - 3
    }


    @Test
    void 상품이_없으면_예외() {
        // given
        Long productId = 999L;
        ProductSale sale = new ProductSale(productId, LocalDate.now(), 1);

        // 비관적 락 조회에서 예외가 발생하도록 설정
        when(productRepository.findWithPessimisticLockById(productId))
            .thenThrow(new IllegalArgumentException("존재하지 않는 상품입니다: " + productId));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(sale));
    }

}

