package kr.hhplus.be.server.infrastructure.product.repository;

// 📦 infrastructure.mock.product

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.ProductSale;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProductSaleRepository implements ProductSaleRepository {

    private final List<ProductSale> sales = new ArrayList<>();

    @Override
    public List<ProductSale> findSalesAfter(LocalDate fromDate) {
        return sales.stream()
            .filter(sale -> sale.getSaleDate().isAfter(fromDate.minusDays(1)))
            .toList();
    }

    @Override
    public void save(ProductSale sale) {
        sales.add(sale);
    }

}

