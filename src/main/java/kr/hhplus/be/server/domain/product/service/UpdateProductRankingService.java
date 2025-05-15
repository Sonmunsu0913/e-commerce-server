package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.infrastructure.product.repository.ProductRankingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 주문 완료 시 Redis에 상품 주문 수량을 반영하여
 * 실시간 인기 상품 랭킹을 업데이트하는 서비스
 */
@Service
public class UpdateProductRankingService {

    private final ProductRankingRepository productRankingRepository;

    public UpdateProductRankingService(ProductRankingRepository productRankingRepository) {
        this.productRankingRepository = productRankingRepository;
    }

    /**
     * 주문된 모든 상품의 주문 수량을 랭킹에 반영 (batch 처리)
     */
    public void increaseOrderCountForItems(List<OrderItemCommand> items) {
        productRankingRepository.increaseScoreBatch(items);
    }
}
