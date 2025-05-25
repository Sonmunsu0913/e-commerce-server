package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.event.OrderReportEvent;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OrderReportEventPublisher {

    private final ApplicationEventPublisher publisher;

    public OrderReportEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(OrderResponse response) {
        publisher.publishEvent(new OrderReportEvent(response));
    }
}