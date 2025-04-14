package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    public ProductRepositoryImpl(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id).map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(ProductEntity::toDomain).toList();
    }

    @Override
    public List<Product> findAllByIdIn(List<Long> ids) {
        return jpaRepository.findAllById(ids).stream().map(ProductEntity::toDomain).toList();
    }

    @Override
    public void save(Product product) {
        jpaRepository.save(ProductEntity.from(product));
    }
}
