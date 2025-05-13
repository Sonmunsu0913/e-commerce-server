package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
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
    public Product save(Product product) {
        ProductEntity savedEntity = jpaRepository.save(ProductEntity.from(product));
        return savedEntity.toDomain();
    }

    @Override
    public Product findWithPessimisticLockById(Long id) {
        return jpaRepository.findWithPessimisticLockById(id).toDomain();
    }

    @Override
    public Product findWithOptimisticLockById(Long productId) {
        return jpaRepository.findWithOptimisticLockById(productId).toDomain();
    }
}
