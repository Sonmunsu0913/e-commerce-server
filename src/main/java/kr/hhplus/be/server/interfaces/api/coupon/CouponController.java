package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.util.*;
import kr.hhplus.be.server.application.coupon.dto.CouponClaimRequest;
import kr.hhplus.be.server.application.coupon.dto.CouponResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@Tag(name = "Coupon", description = "쿠폰 관련 API")
public class CouponController {

    @PostMapping("/claim")
    @Operation(summary = "쿠폰 발급", description = "선착순으로 쿠폰을 발급합니다.")
    public ResponseEntity<CouponResponse> claimCoupon(@RequestBody CouponClaimRequest request) {
        Long userId = request.getUserId();

        CouponResponse response = new CouponResponse(
            101L,
            2000,
            LocalDateTime.now().toString(),
            false
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "보유 쿠폰 목록 조회", description = "사용자가 보유한 쿠폰 목록을 조회합니다.")
    public ResponseEntity<List<CouponResponse>> listCoupons(@PathVariable Long userId) {
        List<CouponResponse> coupons = List.of(
            new CouponResponse(
                101L,
                2000,
                LocalDateTime.now().minusDays(1).toString(),
                false
            )
        );
        return ResponseEntity.ok(coupons);
    }

}

