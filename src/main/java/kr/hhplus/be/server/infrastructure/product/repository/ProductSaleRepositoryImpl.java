package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.infrastructure.product.entity.ProductSaleEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class ProductSaleRepositoryImpl implements ProductSaleRepository {

    private final JpaProductSaleRepository jpaRepository;

    public ProductSaleRepositoryImpl(JpaProductSaleRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(ProductSale sale) {
        jpaRepository.save(ProductSaleEntity.from(sale));
    }

    @Override
    public List<ProductSale> findSalesAfter(LocalDate from) {
        return jpaRepository.findBySaleDateAfter(from).stream()
                .map(ProductSaleEntity::toDomain)
                .toList();
    }
}

