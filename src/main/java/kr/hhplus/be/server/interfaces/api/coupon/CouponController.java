package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import kr.hhplus.be.server.domain.coupon.service.GetUserCouponsService;
import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController {

    private final GetUserCouponsService getUserCouponsService;
    private final IssueCouponService issueCouponService;

    public CouponController(GetUserCouponsService getUserCouponsService, IssueCouponService issueCouponService) {
        this.getUserCouponsService = getUserCouponsService;
        this.issueCouponService = issueCouponService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(getUserCouponsService.execute(userId));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "쿠폰 발급", description = "사용자가 아직 쿠폰을 발급받지 않았다면, 선착순으로 쿠폰을 발급합니다.")
    public ResponseEntity<CouponResponse> issueCoupon(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(issueCouponService.execute(userId));
    }
}


