package kr.hhplus.be.server.application.point.usecase;

import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UsePointUseCase {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UsePointUseCase(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public UserPoint execute(long userId, long amount) {
        UserPoint current = pointRepository.findById(userId);
        UserPoint updated = current.use(amount);
        pointRepository.save(updated);

        LocalDateTime now = LocalDateTime.now();
        PointHistory history = new PointHistory(
            null,
            userId,
            amount,
            PointTransactionType.USE,
            now,
            now
        );
        pointHistoryRepository.save(history);

        return updated;
    }
}
