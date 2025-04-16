package kr.hhplus.be.server.domain.point.usecase;

import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.*;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
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
