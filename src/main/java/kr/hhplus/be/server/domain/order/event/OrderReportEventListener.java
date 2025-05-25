package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderReportEventListener {

    private final MockOrderReporter reporter;

    public OrderReportEventListener(MockOrderReporter reporter) {
        this.reporter = reporter;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderReportEvent event) {
        reporter.send(event.getResponse());
    }
}
