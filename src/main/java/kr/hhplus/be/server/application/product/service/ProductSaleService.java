package kr.hhplus.be.server.application.product.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductSaleService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public ProductSaleService(ProductSaleRepository productSaleRepository, ProductRepository productRepository) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
    }


    // 특정 날짜 이후의 판매 내역 조회
    public List<ProductSale> findSalesAfter(LocalDate fromDate) {
        return productSaleRepository.findSalesAfter(fromDate);
    }

    // 판매 기록 저장 + 재고 감소 처리
    public void recordSale(ProductSale sale) {
        // 1. 판매 이력 저장
        productSaleRepository.save(sale);

        // 2. 상품 조회
        Product product = productRepository.findById(sale.productId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + sale.productId()));

        // 3. 재고 감소
        product.reduceStock(sale.quantity());

        // 4. 상품 저장
        productRepository.save(product);
    }
}
