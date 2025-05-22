package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.event.PointEvent;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
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
    public void publishRequest(CreateOrderCommand command) {
        publisher.publishEvent(new OrderRequestEvent(command));
    }

    /**
     * 주문 생성 완료 이벤트 발행
     */
    public void publishCreated(Order order) {
        publisher.publishEvent(new OrderCreatedEvent(order));
    }

    /**
     * 포인트 차감 완료 이벤트 발행
     */
    public void publishPoint(Order order, UserPoint point) {
        publisher.publishEvent(new PointEvent(order, point));
    }

    /**
     * 상품 판매 기록 저장 및 랭킹 반영 이벤트 발행
     * 단일 OrderSaleEvent를 통해 두 작업이 함께 처리
     */
    public void publishSale(Order order) {
        publisher.publishEvent(new OrderSaleEvent(order));
    }

    /**
     * 외부 리포트 전송 이벤트 발행
     */
    public void publishReport(OrderResponse response) {
        publisher.publishEvent(new OrderReportEvent(response));
    }
}
