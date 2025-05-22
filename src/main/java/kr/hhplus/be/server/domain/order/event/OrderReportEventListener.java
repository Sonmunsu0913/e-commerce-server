package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderReportEventListener {

    private final MockOrderReporter reporter;

    public OrderReportEventListener(MockOrderReporter reporter) {
        this.reporter = reporter;
    }

    @EventListener
    public void handle(OrderReportEvent event) {
        reporter.send(event.getResponse());
    }
}
