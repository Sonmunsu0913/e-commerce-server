package kr.hhplus.be.server.infrastructure.coupon.kafka;

import kr.hhplus.be.server.interfaces.api.coupon.kafka.CouponIssueRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueEventPublisher {

    private final KafkaTemplate<String, CouponIssueRequest> kafkaTemplate;

    public CouponIssueEventPublisher(KafkaTemplate<String, CouponIssueRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Long userId, Long couponId) {
        CouponIssueRequest message = new CouponIssueRequest(userId, couponId);
        String partitionKey = String.valueOf(couponId);

        System.out.println("[Kafka 발행] " + message);
        kafkaTemplate.send("coupon.issue.request", partitionKey, message);
    }
}
