package kr.hhplus.be.server.interfaces.api.report.kafka;

import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.infrastructure.report.kafka.OrderReportMessage;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class OrderReportListener {

    private final MockOrderReporter reporter;

    public OrderReportListener(MockOrderReporter reporter) {
        this.reporter = reporter;
    }

    @KafkaListener(topics = "order-report-topic", groupId = "report-group")
    public void listen(OrderReportMessage message, Acknowledgment ack) {
        try {
            // 메시지 처리 로직
            reporter.sendMessage(message);
            System.out.println("받은 메시지: " + message);
            // 처리 성공 시에만 offset 커밋
            ack.acknowledge();
        } catch (Exception e) {
            // 처리 실패 → 커밋 안함 → 재처리 가능
            System.err.println("Kafka 메시지 처리 실패: " + e.getMessage());
        }
    }
}

