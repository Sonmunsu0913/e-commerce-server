package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

import kr.hhplus.be.server.domain.coupon.usecase.GetUserCouponsUseCase;
import kr.hhplus.be.server.domain.coupon.usecase.IssueCouponUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@Tag(name = "Coupon", description = "쿠폰 API")
public class CouponController {

    private final GetUserCouponsUseCase getUserCouponsUseCase;
    private final IssueCouponUseCase issueCouponUseCase;

    public CouponController(GetUserCouponsUseCase getUserCouponsUseCase, IssueCouponUseCase issueCouponUseCase) {
        this.getUserCouponsUseCase = getUserCouponsUseCase;
        this.issueCouponUseCase = issueCouponUseCase;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> getMyCoupons(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(getUserCouponsUseCase.execute(userId));
    }

    @PostMapping("/{userId}")
    @Operation(summary = "쿠폰 발급", description = "사용자가 아직 쿠폰을 발급받지 않았다면, 선착순으로 쿠폰을 발급합니다.")
    public ResponseEntity<CouponResponse> issueCoupon(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable Long userId
    ) {
        final Long fixedCouponId = 1L;
        return ResponseEntity.ok(issueCouponUseCase.execute(userId, fixedCouponId));
    }
}


