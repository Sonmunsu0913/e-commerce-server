package kr.hhplus.be.server.application.product.repository;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.product.ProductSale;

public interface ProductSaleRepository {

    List<ProductSale> findSalesAfter(LocalDate fromDate);

}
