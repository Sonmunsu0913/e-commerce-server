package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.*;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsePointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UsePointService(UserPointRepository userPointRepository, PointHistoryRepository pointHistoryRepository) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public UserPoint execute(long userId, long amount) {
        UserPoint current = userPointRepository.findById(userId);
        UserPoint updated = current.use(amount);
        userPointRepository.save(updated);

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

    public void init(Long userId) {
        userPointRepository.save(new UserPoint(userId, 0L, LocalDateTime.now(), LocalDateTime.now()));
    }
}
