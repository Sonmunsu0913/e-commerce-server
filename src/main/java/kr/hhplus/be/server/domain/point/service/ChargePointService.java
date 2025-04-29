package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.*;

import java.time.LocalDateTime;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChargePointService {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public ChargePointService(UserPointRepository userPointRepository, PointHistoryRepository pointHistoryRepository) {
        this.userPointRepository = userPointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public UserPoint execute(long userId, long amount) {

        // 1. 락을 걸고 사용자 포인트 조회
        UserPoint current = userPointRepository.findWithPessimisticLockById(userId);
//        UserPoint current = userPointRepository.findWithOptimisticLockById(userId);

        // 2. 포인트 충전
        UserPoint updated = current.charge(amount);

        // 3. 충전된 포인트 저장
        userPointRepository.save(updated);
//        try {
//            userPointRepository.save(updated);
//        } catch (ObjectOptimisticLockingFailureException e) {
//            throw new IllegalStateException("포인트 충전 중 충돌이 발생했습니다. 다시 시도해주세요.");
//        }

        // 4. 포인트 충전 내역 저장
        LocalDateTime now = LocalDateTime.now();
        PointHistory history = new PointHistory(
            null,
            userId,
            amount,
            PointTransactionType.CHARGE,
            now,
            now
        );
        pointHistoryRepository.save(history);

        return updated;
    }
}
