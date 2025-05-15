package kr.hhplus.be.server.infrastructure.scheduler;

import kr.hhplus.be.server.domain.coupon.service.RedisCouponQueueService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RedisCouponQueueScheduler {

    private final RedisCouponQueueService redisCouponQueueService;

    public RedisCouponQueueScheduler(RedisCouponQueueService redisCouponQueueService) {
        this.redisCouponQueueService = redisCouponQueueService;
    }

    @Scheduled(fixedDelay = 5000)
    public void processCouponQueue() {
        Long couponId = 100L; // 실제 서비스라면 쿠폰 ID 리스트를 순회하는 방식으로 개선 필요
        for (int i = 0; i < 5; i++) {
            String userIdStr = redisCouponQueueService.popFromQueue(couponId);
            if (userIdStr == null) break;

            boolean issued = redisCouponQueueService.tryIssueCoupon(couponId, Long.valueOf(userIdStr));
            if (issued) {
                System.out.println("[대기열 발급 성공] userId = " + userIdStr);
            } else {
                System.out.println("[발급 실패] userId = " + userIdStr);
            }
        }
    }
}
