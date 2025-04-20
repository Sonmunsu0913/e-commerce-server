package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.infrastructure.product.entity.ProductSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface JpaProductSaleRepository extends JpaRepository<ProductSaleEntity, Long> {
    List<ProductSaleEntity> findBySaleDateAfter(LocalDate from);
}
