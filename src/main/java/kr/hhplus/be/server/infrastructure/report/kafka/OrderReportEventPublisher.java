package kr.hhplus.be.server.infrastructure.report.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderReportEventPublisher {

    private final KafkaTemplate<String, OrderReportMessage> kafkaTemplate;

    public OrderReportEventPublisher(KafkaTemplate<String, OrderReportMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderReportMessage message) {
        try {
            kafkaTemplate.send("order-report-topic", message);
        } catch (Exception e) {
            // 로깅만 하고 예외는 흘려보내지 않음
            // 테스트나 운영 환경에 따라 필요시 Alert 처리
            System.err.println("[Kafka 전송 실패] " + e.getMessage());
        }
    }
}
