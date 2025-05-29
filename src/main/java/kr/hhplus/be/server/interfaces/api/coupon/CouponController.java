package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import kr.hhplus.be.server.application.order.CouponFacade;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.domain.coupon.service.RedisCouponQueueService;
import kr.hhplus.be.server.infrastructure.coupon.kafka.CouponIssueEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController {

    private final GetUserCouponService getUserCouponService;
    private final IssueCouponService issueCouponService;
    private final CouponFacade couponFacade;
    private final CouponIssueEventPublisher couponIssueEventPublisher;

    public CouponController(GetUserCouponService getUserCouponService, IssueCouponService issueCouponService
                            , CouponFacade couponFacade, CouponIssueEventPublisher couponIssueEventPublisher) {
        this.getUserCouponService = getUserCouponService;
        this.issueCouponService = issueCouponService;
        this.couponFacade = couponFacade;
        this.couponIssueEventPublisher = couponIssueEventPublisher;
    }

    @GetMapping("/available/{userId}")
    @Operation(summary = "발급 가능한 쿠폰 목록 조회", description = "잔여 수량이 있고, 아직 사용자가 발급받지 않은 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getAvailableCoupons(@PathVariable Long userId) {
        return ResponseEntity.ok(issueCouponService.getAvailableCoupons(userId));
    }


    @GetMapping("/{userId}")
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(getUserCouponService.execute(userId));
    }

    @PostMapping("/{userId}/coupon/{couponId}")
    @Operation(summary = "쿠폰 발급", description = "사용자가 아직 쿠폰을 발급받지 않았다면, 선착순으로 쿠폰을 발급합니다.")
    public ResponseEntity<CouponResponse> issueCoupon(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId,
            @Parameter(description = "쿠폰 ID", example = "100")
            @PathVariable Long couponId
    ) {
        return ResponseEntity.ok(issueCouponService.execute(userId, couponId));
    }

    @PostMapping("/v2/{userId}/coupon/{couponId}")
    @Operation(summary = "쿠폰 발급 (v2)", description = "선착순으로 발급, 초과 시 대기열 등록")
    public ResponseEntity<String> issueCouponV2(
        @PathVariable Long userId,
        @PathVariable Long couponId) {

        boolean success = couponFacade.issueCouponV2(userId, couponId);

        if (success) {
            return ResponseEntity.ok("쿠폰 발급 성공");
        } else {
            return ResponseEntity.ok("대기열에 등록됨");
        }
    }


    @PostMapping("/v3/{userId}/coupon/{couponId}")
    @Operation(summary = "쿠폰 발급 (v3)", description = "Kafka 큐 기반 발급 요청")
    public ResponseEntity<String> issueCouponV3(@PathVariable Long userId, @PathVariable Long couponId) {
        couponIssueEventPublisher.publish(userId, couponId);
        return ResponseEntity.ok("쿠폰 발급 요청이 접수되었습니다.");
    }
}


