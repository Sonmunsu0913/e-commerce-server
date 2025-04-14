package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.point.usecase.*;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.point.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@Tag(name = "Point", description = "포인트 관련 API")
public class PointController {

    private final ChargePointUseCase chargePointUseCase;
    private final UsePointUseCase usePointUseCase;
    private final GetUserPointUseCase getUserPointUseCase;
    private final GetPointHistoryUseCase getPointHistoryUseCase;

    public PointController(
        ChargePointUseCase chargePointUseCase,
        UsePointUseCase usePointUseCase,
        GetUserPointUseCase getUserPointUseCase,
        GetPointHistoryUseCase getPointHistoryUseCase
    ) {
        this.chargePointUseCase = chargePointUseCase;
        this.usePointUseCase = usePointUseCase;
        this.getUserPointUseCase = getUserPointUseCase;
        this.getPointHistoryUseCase = getPointHistoryUseCase;
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    public ResponseEntity<PointResponse> charge(@RequestBody PointChargeRequest request) {
        UserPoint charged = chargePointUseCase.execute(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(charged));
    }

    @PostMapping("/use")
    @Operation(summary = "포인트 사용", description = "사용자의 포인트를 사용합니다.")
    public ResponseEntity<PointResponse> use(@RequestBody PointUseRequest request) {
        UserPoint used = usePointUseCase.execute(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(used));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    public ResponseEntity<PointResponse> getPoint(@PathVariable Long userId) {
        UserPoint point = getUserPointUseCase.execute(userId);
        return ResponseEntity.ok(PointResponse.from(point));
    }

    @GetMapping("/{userId}/histories")
    @Operation(summary = "포인트 내역 조회", description = "사용자의 포인트 충전/사용 내역을 조회합니다.")
    public ResponseEntity<List<PointHistoryResponse>> getHistory(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(
            getPointHistoryUseCase.execute(userId).stream()
                .map(PointHistoryResponse::from)
                .toList()
        );
    }
}