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

    public UserPoint getPoint(long userId) {
        return pointRepository.findById(userId);
    }

    public UserPoint charge(long userId, long amount) {
        return updatePointWithHistory(userId, amount, PointTransactionType.CHARGE);
    }

    public UserPoint use(long userId, long amount) {
        return updatePointWithHistory(userId, amount, PointTransactionType.USE);
    }

    private UserPoint updatePointWithHistory(long userId, long amount, PointTransactionType type) {
        UserPoint current = pointRepository.findById(userId);
        UserPoint updated = switch (type) {
            case CHARGE -> current.charge(amount);
            case USE -> current.use(amount);
        };

        pointRepository.save(updated);

        PointHistory history = new PointHistory(
            null,
            userId,
            amount,
            type,
            LocalDateTime.now()
        );
        pointHistoryRepository.save(history);

        return updated;
    }
}