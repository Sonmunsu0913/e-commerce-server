package kr.hhplus.be.server.application.product.usecase;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import org.springframework.stereotype.Service;

/**
 * 상품 판매 기록 + 재고 감소 유스케이스
 */
@Service
public class RecordProductSaleUseCase {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public RecordProductSaleUseCase(ProductSaleRepository productSaleRepository, ProductRepository productRepository) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
    }

    public void execute(ProductSale sale) {
        // 1. 판매 이력 저장
        productSaleRepository.save(sale);

        // 2. 상품 조회
        Product product = productRepository.findById(sale.productId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다: " + sale.productId()));

        // 3. 재고 감소
        Product updatedProduct = product.reduceStock(sale.quantity());

        // 4. 상품 저장
        productRepository.save(updatedProduct);
    }
}
