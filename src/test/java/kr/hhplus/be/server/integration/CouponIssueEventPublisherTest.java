package kr.hhplus.be.server.integration;

import static org.mockito.Mockito.verify;

import kr.hhplus.be.server.infrastructure.coupon.kafka.CouponIssueEventPublisher;
import kr.hhplus.be.server.interfaces.api.coupon.kafka.CouponIssueRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

@ExtendWith(MockitoExtension.class)
class CouponIssueEventPublisherTest {

    @Mock
    KafkaTemplate<String, CouponIssueRequest> kafkaTemplate;

    @InjectMocks
    CouponIssueEventPublisher publisher;

    @Test
    void publish_shouldSendMessageToKafka() {
        // given
        Long userId = 1L;
        Long couponId = 100L;
        CouponIssueRequest expectedMessage = new CouponIssueRequest(userId, couponId);

        // when
        publisher.publish(userId, couponId);
        System.out.println("Kafka publish called with: " + expectedMessage);

        // then
        verify(kafkaTemplate).send("coupon.issue.request", expectedMessage);
    }
}

