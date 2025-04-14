package kr.hhplus.be.server.application.product.usecase;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 전체 상품 목록 조회 유스케이스
 */
@Component
public class GetAllProductsUseCase {

    private final ProductRepository productRepository;

    public GetAllProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> execute() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(
                        product.id(),
                        product.name(),
                        product.price(),
                        product.stock()
                )).toList();
    }
}