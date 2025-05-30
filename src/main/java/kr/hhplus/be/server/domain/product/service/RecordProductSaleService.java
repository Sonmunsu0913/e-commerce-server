package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 상품 판매 기록 + 재고 감소 유스케이스
 */
@Service
@Transactional
public class RecordProductSaleService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public RecordProductSaleService(ProductSaleRepository productSaleRepository, ProductRepository productRepository) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
    }

    public void execute(ProductSale sale) {
        // 1. 판매 이력 저장
        productSaleRepository.save(sale);

        // 2. 상품 조회 (락) + 예외 처리
        Product product = productRepository.findWithPessimisticLockById(sale.productId());
//        Product product = productRepository.findWithOptimisticLockById(sale.productId());

        if (product == null) {
            throw new IllegalArgumentException("존재하지 않는 상품입니다: " + sale.productId());
        }

        // 3. 재고 감소
        Product updatedProduct = product.reduceStock(sale.quantity());

        // 4. 상품 저장
        productRepository.save(updatedProduct);
    }

}
