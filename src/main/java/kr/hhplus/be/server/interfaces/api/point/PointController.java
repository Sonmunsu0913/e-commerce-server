package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.point.service.ChargePointService;
import kr.hhplus.be.server.domain.point.service.GetPointHistoryService;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/point")
@Tag(name = "Point", description = "포인트 관련 API")
public class PointController {

    private final ChargePointService chargePointService;
    private final UsePointService usePointService;
    private final GetUserPointService getUserPointService;
    private final GetPointHistoryService getPointHistoryService;

    public PointController(
        ChargePointService chargePointService,
        UsePointService usePointService,
        GetUserPointService getUserPointService,
        GetPointHistoryService getPointHistoryService
    ) {
        this.chargePointService = chargePointService;
        this.usePointService = usePointService;
        this.getUserPointService = getUserPointService;
        this.getPointHistoryService = getPointHistoryService;
    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    public ResponseEntity<PointResponse> charge(@RequestBody PointChargeRequest request) {
        UserPoint charged = chargePointService.execute(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(charged));
    }

    @PostMapping("/use")
    @Operation(summary = "포인트 사용", description = "사용자의 포인트를 사용합니다.")
    public ResponseEntity<PointResponse> use(@RequestBody PointUseRequest request) {
        UserPoint used = usePointService.execute(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(used));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    public ResponseEntity<PointResponse> getPoint(@PathVariable Long userId) {
        UserPoint point = getUserPointService.execute(userId);
        return ResponseEntity.ok(PointResponse.from(point));
    }

    @GetMapping("/{userId}/histories")
    @Operation(summary = "포인트 내역 조회", description = "사용자의 포인트 충전/사용 내역을 조회합니다.")
    public ResponseEntity<List<PointHistoryResponse>> getHistory(@PathVariable("userId") long userId) {
        return ResponseEntity.ok(
            getPointHistoryService.execute(userId).stream()
                .map(PointHistoryResponse::from)
                .toList()
        );
    }
}