package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        List<CouponResponse> responses = couponService.getUserCoupons(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @Operation(summary = "쿠폰 발급", description = "사용자가 아직 쿠폰을 발급받지 않았다면, 선착순으로 쿠폰을 발급합니다.")
    public ResponseEntity<CouponResponse> issueCoupon(
        @Parameter(description = "사용자 ID", example = "1")
        @PathVariable Long userId
    ) {
        final Long fixedCouponId = 1L;

        // 서비스에서 발급 가능 여부를 확인하고 처리
        CouponResponse response = couponService.issueCoupon(userId, fixedCouponId);

        return ResponseEntity.ok(response);
    }
}


