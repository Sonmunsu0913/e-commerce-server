package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.service.CreateProductService;
import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import kr.hhplus.be.server.infrastructure.product.repository.JpaProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateProductServiceTest {

    private JpaProductRepository productRepository;
    private CreateProductService createProductService;

    @BeforeEach
    void setUp() {
        // Mock 초기화
        productRepository = mock(JpaProductRepository.class);
        createProductService = new CreateProductService(productRepository);
    }

    @Test
    void 상품을_정상적으로_등록한다() {
        // given
        CreateProductCommand command = new CreateProductCommand("마카롱 세트", 15000, 100);

        // when
        createProductService.create(command);

        // then
        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(productRepository, times(1)).save(captor.capture());

        ProductEntity savedProduct = captor.getValue();
        assertThat(savedProduct.getName()).isEqualTo("마카롱 세트");
        assertThat(savedProduct.getPrice()).isEqualTo(15000);
        assertThat(savedProduct.getStock()).isEqualTo(100);
    }
}
