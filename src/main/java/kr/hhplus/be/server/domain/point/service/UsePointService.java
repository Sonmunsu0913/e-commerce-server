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

    /**
     * 사용자의 포인트를 차감합니다.
     * - 동시성 문제를 방지하기 위해 Pessimistic Lock을 사용하여 UserPoint를 조회합니다.
     * - 트랜잭션으로 묶어 정합성을 보장합니다.
     *
     * @param userId 사용자 ID
     * @param amount 차감할 포인트
     * @return 차감 후의 UserPoint 객체
     */
    public UserPoint execute(long userId, long amount) {

        // 1. 락을 걸고 사용자 포인트 조회
        UserPoint current = userPointRepository.findWithPessimisticLockById(userId);

        // 2. 포인트 차감
        UserPoint updated = current.use(amount);

        // 3. 차감된 포인트 저장
        userPointRepository.save(updated);

        // 4. 포인트 사용 내역 저장
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
