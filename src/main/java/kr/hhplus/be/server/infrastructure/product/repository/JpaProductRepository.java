package kr.hhplus.be.server.infrastructure.product.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infrastructure.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaProductRepository extends JpaRepository<ProductEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p WHERE p.id = :id")
    ProductEntity findWithPessimisticLockById(@Param("id") Long id);
}