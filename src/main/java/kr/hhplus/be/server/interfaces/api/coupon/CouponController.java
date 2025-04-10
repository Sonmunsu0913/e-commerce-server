package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.application.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.application.coupon.dto.CouponResponse;
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

    @GetMapping("/me")
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 발급받은 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(@RequestParam Long userId) {
        List<CouponResponse> responses = couponService.getUserCoupons(userId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{couponId}/issue")
    @Operation(summary = "선착순 쿠폰 발급", description = "선착순으로 쿠폰을 발급받습니다.")
    public ResponseEntity<CouponResponse> issueCoupon(
        @PathVariable Long couponId,
        @RequestBody CouponIssueRequest request
    ) {
        CouponResponse response = couponService.issueCoupon(request.getUserId(), couponId);
        return ResponseEntity.ok(response);
    }
}


