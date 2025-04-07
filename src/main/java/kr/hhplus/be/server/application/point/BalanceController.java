package kr.hhplus.be.server.application.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import kr.hhplus.be.server.application.point.dto.BalanceChargeRequest;
import kr.hhplus.be.server.application.point.dto.BalanceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balance")
@Tag(name = "Balance", description = "잔액 관련 API")
public class BalanceController {

    @PostMapping("/charge")
    @Operation(summary = "잔액 충전", description = "사용자의 잔액을 충전합니다.")
    public ResponseEntity<Map<String, Object>> charge(@RequestBody BalanceChargeRequest request) {
        Long userId = request.getUserId();
        Integer amount = request.getAmount();

        Map<String, Object> response = Map.of(
            "userId", userId,
            "balance", amount + 10000
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "잔액 조회", description = "사용자의 잔액을 조회합니다.")
    public ResponseEntity<BalanceResponse> getBalance(@PathVariable Long userId) {
        // 실제 로직이 없으므로 Mock 응답 반환
        BalanceResponse response = new BalanceResponse(userId, 60000);
        return ResponseEntity.ok(response);
    }

}

