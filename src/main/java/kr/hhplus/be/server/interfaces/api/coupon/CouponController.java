package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController {

    private final GetUserCouponService getUserCouponService;
    private final IssueCouponService issueCouponService;

    public CouponController(GetUserCouponService getUserCouponService, IssueCouponService issueCouponService) {
        this.getUserCouponService = getUserCouponService;
        this.issueCouponService = issueCouponService;
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
}


