package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.CreateProductCommand;
import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import kr.hhplus.be.server.infrastructure.product.repository.JpaProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateProductService {

    private final JpaProductRepository productRepository;

    public CreateProductService(JpaProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void create(CreateProductCommand command) {
        ProductEntity product = new ProductEntity(
            null,
            command.getName(),
            command.getPrice(),
            command.getStock()
        );
        productRepository.save(product);
    }
}
