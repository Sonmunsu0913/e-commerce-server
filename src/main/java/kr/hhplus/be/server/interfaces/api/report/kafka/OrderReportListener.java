package kr.hhplus.be.server.interfaces.api.report.kafka;

import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.infrastructure.report.kafka.OrderReportMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderReportListener {

    private final MockOrderReporter reporter;

    public OrderReportListener(MockOrderReporter reporter) {
        this.reporter = reporter;
    }

    @KafkaListener(topics = "order-report-topic", groupId = "report-group")
    public void listen(OrderReportMessage message) {
        reporter.sendMessage(message);
    }
}

