package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import java.util.stream.Collectors;

import kr.hhplus.be.server.application.point.service.PointHistoryService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.point.dto.PointChargeRequest;
import kr.hhplus.be.server.interfaces.api.point.dto.PointHistoryResponse;
import kr.hhplus.be.server.interfaces.api.point.dto.PointResponse;
import kr.hhplus.be.server.interfaces.api.point.dto.PointUseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/point")
@Tag(name = "Point", description = "포인트 관련 API")
public class PointController {

    private final PointService pointService;
    private final PointHistoryService pointHistoryService;


    public PointController(PointService pointService, PointHistoryService pointHistoryService) {
        this.pointService = pointService;
        this.pointHistoryService = pointHistoryService;

    }

    @PostMapping("/charge")
    @Operation(summary = "포인트 충전", description = "사용자의 포인트를 충전합니다.")
    public ResponseEntity<PointResponse> charge(@RequestBody PointChargeRequest request) {
        UserPoint charged = pointService.charge(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(charged));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "포인트 조회", description = "사용자의 포인트를 조회합니다.")
    public ResponseEntity<PointResponse> getPoint(@PathVariable Long userId) {
        UserPoint point = pointService.getPoint(userId);
        return ResponseEntity.ok(PointResponse.from(point));
    }

    @GetMapping("/{userId}/histories")
    @Operation(summary = "포인트 내역 조회", description = "사용자의 포인트 충전/사용 내역을 조회합니다.")
    public List<PointHistory> getHistory(@PathVariable long id) {
        return pointHistoryService.getHistory(id);
    }

    @PostMapping("/use")
    @Operation(summary = "포인트 사용", description = "사용자의 포인트를 사용합니다.")
    public ResponseEntity<PointResponse> use(@RequestBody PointUseRequest request) {
        UserPoint used = pointService.use(request.getUserId(), request.getAmount());
        return ResponseEntity.ok(PointResponse.from(used));
    }

}

