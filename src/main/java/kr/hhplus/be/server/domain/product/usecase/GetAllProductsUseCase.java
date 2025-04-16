package kr.hhplus.be.server.domain.product.usecase;

import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.interfaces.api.product.ProductResponse;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 전체 상품 목록 조회 유스케이스
 */
@Service
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