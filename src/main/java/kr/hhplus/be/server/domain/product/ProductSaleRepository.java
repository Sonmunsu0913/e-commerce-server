package kr.hhplus.be.server.domain.product;

import java.time.LocalDate;
import java.util.List;

public interface ProductSaleRepository {

    List<ProductSale> findSalesAfter(LocalDate fromDate);

    void save(ProductSale sale);

}
