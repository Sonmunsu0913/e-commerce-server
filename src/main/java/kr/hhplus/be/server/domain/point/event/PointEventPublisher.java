package kr.hhplus.be.server.domain.point.event;

import kr.hhplus.be.server.domain.order.Order;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 포인트 관련 도메인 이벤트를 발행하는 퍼블리셔 클래스.
 * 내부적으로 Spring의 ApplicationEventPublisher를 사용한다.
 */
@Component
public class PointEventPublisher {

    private final ApplicationEventPublisher publisher;

    public PointEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    /**
     * 포인트 차감 완료 이벤트 발행
     */
    public void publishPoint(Order order) {
        publisher.publishEvent(new PointEvent(order));
    }

}

