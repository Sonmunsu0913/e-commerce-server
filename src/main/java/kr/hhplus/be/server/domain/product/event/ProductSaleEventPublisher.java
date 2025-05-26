package kr.hhplus.be.server.domain.product.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 상품 판매 처리 이벤트를 발행하는 퍼블리셔.
 * 내부적으로 Spring의 ApplicationEventPublisher를 사용한다.
 */
@Component
public class ProductSaleEventPublisher {

    private final ApplicationEventPublisher publisher;

    public ProductSaleEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 상품 판매 기록 저장 및 랭킹 반영 이벤트 발행
     */
    public void publishSale(Order order) {
        publisher.publishEvent(new ProductSaleEvent(order));
    }
}
