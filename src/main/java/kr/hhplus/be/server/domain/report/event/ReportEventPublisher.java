package kr.hhplus.be.server.domain.report.event;

import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReportEventPublisher {

    private final ApplicationEventPublisher publisher;

    public ReportEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishReport(OrderResponse response) {
        publisher.publishEvent(new OrderReportEvent(response));
    }
}
