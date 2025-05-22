package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.service.RecordProductSaleService;
import kr.hhplus.be.server.domain.product.service.UpdateProductRankingService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;

/**
 * 주문 판매 처리 리스너.
 * - 상품 판매 기록 저장
 * - Redis 랭킹 반영
 */
@Component
public class OrderSaleEventListener {

    private final RecordProductSaleService recordProductSaleService;
    private final UpdateProductRankingService updateProductRankingService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderSaleEventListener(RecordProductSaleService recordProductSaleService,
                                  UpdateProductRankingService updateProductRankingService,
                                  OrderEventPublisher orderEventPublisher) {
        this.recordProductSaleService = recordProductSaleService;
        this.updateProductRankingService = updateProductRankingService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(OrderSaleEvent event) {
        Order order = event.getOrder();
        UserPoint point = event.getPoint();
        LocalDate today = LocalDate.now();

        // 1. 상품 판매 기록 저장
        order.getItems().forEach(item -> {
            ProductSale sale = new ProductSale(item.productId(), today, item.quantity());
            recordProductSaleService.execute(sale);
        });

        // 2. Redis 랭킹 반영
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                updateProductRankingService.increaseOrderCountForItems(order.getItems());
            }
        });

        // 6. 외부 리포트 전송 이벤트 발행
        orderEventPublisher.publishReport(order.toResponse((int) point.point()));
    }
}
