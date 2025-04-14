package kr.hhplus.be.server.application.point.service;

import java.time.LocalDateTime;
import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public PointService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    // 사용자 포인트 조회
    public UserPoint getPoint(long userId) {
        return pointRepository.findById(userId);
    }

    // 포인트 충전
    public UserPoint charge(long userId, long amount) {
        return updatePointWithHistory(userId, amount, PointTransactionType.CHARGE);
    }

    // 포인트 사용
    public UserPoint use(long userId, long amount) {
        return updatePointWithHistory(userId, amount, PointTransactionType.USE);
    }

    // 포인트 충전/사용 처리 및 히스토리 기록
    private UserPoint updatePointWithHistory(long userId, long amount, PointTransactionType type) {
        // 현재 포인트 조회
        UserPoint current = pointRepository.findById(userId);

        // 포인트 업데이트
        UserPoint updated = current.handle(type, amount);
        pointRepository.save(updated);

        // 이력 저장
        PointHistory history = new PointHistory(
            null,
            userId,
            amount,
            type,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        pointHistoryRepository.save(history);

        return updated;
    }
}
