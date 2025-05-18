package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.domain.coupon.service.RedisCouponQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponFacade {

    private final RedisCouponQueueService redisCouponQueueService;
    private final IssueCouponService issueCouponService;

    /**
     * Redis 기반 선착순 쿠폰 발급 처리
     * - Redis에서 중복 및 재고 확인 후 발급 가능 여부 판단
     * - 발급 성공 시 DB에 영속적으로 쿠폰 발급 이력 저장
     *
     * @param userId 발급 요청 사용자 ID
     * @param couponId 발급 대상 쿠폰 ID
     * @return 발급 성공 여부 (true: 발급 성공 및 DB 저장, false: 대기열 등록 등 실패)
     */
    public boolean issueCouponV2(Long userId, Long couponId) {
        // Redis Lua 스크립트로 중복 발급 및 재고 초과 여부 판단
        boolean issued = redisCouponQueueService.tryIssueCoupon(couponId, userId);

        if (issued) {
            // Redis 발급 성공 → 트랜잭션 기반 DB 발급 이력 저장 (source of truth 보장)
            issueCouponService.issueCouponWithTx(userId, couponId);
        }

        return issued;
    }
}
