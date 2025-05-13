package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.domain.order.OrderItemCommand;
import java.util.List;

/**
 * 상품 주문 랭킹을 저장소에 반영하기 위한 인터페이스
 * (Redis 등 구현체에 따라 실제 저장 방식은 달라질 수 있음)
 */
public interface ProductRankingRepository {

    /**
     * 단일 상품의 주문 수량을 랭킹에 반영
     */
    void increaseScore(Long productId, int quantity);

    /**
     * 복수 상품을 한 번에 처리하여 랭킹에 반영
     */
    void increaseScoreBatch(List<OrderItemCommand> items);
}
