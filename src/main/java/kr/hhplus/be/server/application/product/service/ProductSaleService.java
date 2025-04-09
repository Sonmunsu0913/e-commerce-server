package kr.hhplus.be.server.application.product.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductSaleService {

    private final ProductSaleRepository productSaleRepository;

    public ProductSaleService(ProductSaleRepository productSaleRepository) {
        this.productSaleRepository = productSaleRepository;
    }

    public List<ProductSale> findSalesAfter(LocalDate fromDate) {
        return productSaleRepository.findSalesAfter(fromDate);
    }

    public void recordSale(ProductSale sale) {
        productSaleRepository.save(sale);
    }
}

