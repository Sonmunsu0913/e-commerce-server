package kr.hhplus.be.server.example.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    @KafkaListener(topics = "test-topic", groupId = "test-consumer")
    public void listen(String message) {
        System.out.println("수신된 메시지: " + message);
    }
}
