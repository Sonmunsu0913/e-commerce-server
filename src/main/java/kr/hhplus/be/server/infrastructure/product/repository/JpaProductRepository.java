package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {
}