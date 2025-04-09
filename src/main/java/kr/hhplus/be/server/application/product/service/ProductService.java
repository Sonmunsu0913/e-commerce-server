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

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
            .map(p -> new ProductResponse(p.id(), p.name(), p.price(), p.stock()))
            .toList();
    }
}
