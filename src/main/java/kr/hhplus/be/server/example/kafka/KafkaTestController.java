package kr.hhplus.be.server.example.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    private final KafkaMessageProducer producer;

    public KafkaTestController(KafkaMessageProducer producer) {
        this.producer = producer;
    }

    @GetMapping("/send")
    public String send(@RequestParam String message) {
        producer.send("test-topic", message);
        return "메시지 전송: " + message;
    }
}

