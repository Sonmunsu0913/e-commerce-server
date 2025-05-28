package kr.hhplus.be.server.domain.order.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 주문 관련 도메인 이벤트를 발행하는 퍼블리셔 클래스.
 * 내부적으로 Spring의 ApplicationEventPublisher를 사용한다.
 */
@Component
public class OrderEventPublisher {

    private final ApplicationEventPublisher publisher;

    public OrderEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 주문 요청 이벤트 발행 (쿠폰 검증 및 주문 생성 시작)
     */
    public void publishRequest(OrderRequestedEventData eventData) {
        publisher.publishEvent(new OrderRequestEvent(eventData));
    }

}
