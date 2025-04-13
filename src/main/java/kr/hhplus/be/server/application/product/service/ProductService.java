package kr.hhplus.be.server.application.product.service;

import java.util.List;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 전체 상품 목록을 조회하고 DTO로 매핑
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
            .map(product -> new ProductResponse(
                product.id(),
                product.name(),
                product.price(),
                product.stock()
            ))
            .toList();
    }
}

