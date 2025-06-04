package kr.hhplus.be.server.interfaces.api.coupon.kafka;

import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueRequestListener {

    private final IssueCouponService issueCouponService;

    public CouponIssueRequestListener(IssueCouponService issueCouponService) {
        this.issueCouponService = issueCouponService;
    }

    @KafkaListener(topics = "coupon.issue.request", groupId = "coupon-service")
    public void consume(CouponIssueRequest request, Acknowledgment ack) {
        System.out.println("Kafka 메시지 수신: " + request); // consume 확인용 로그
        try {
            issueCouponService.issueCouponWithTx(request.userId(), request.couponId());
            ack.acknowledge();
        } catch (Exception e) {
            System.err.println("[쿠폰 발급 실패] userId=" + request.userId() + ", reason=" + e.getMessage());
        }
    }
}

