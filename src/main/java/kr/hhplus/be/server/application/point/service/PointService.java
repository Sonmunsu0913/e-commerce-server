package kr.hhplus.be.server.application.point.service;

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

    public UserPoint getPoint(long id) {
        return pointRepository.findById(id);
    }

    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        UserPoint chargedPoint = userPoint.charge(amount);

        pointRepository.save(chargedPoint);

        PointHistory history = new PointHistory(
            null, // ID는 내부적으로 시퀀스에서 할당 (InMemory or JPA)
            id,
            amount,
            PointTransactionType.CHARGE,
            chargedPoint.updateMillis()
        );
        pointHistoryRepository.save(history);
        return chargedPoint;
    }

    public UserPoint use(long id, long amount) {
        UserPoint userPoint = pointRepository.findById(id);
        UserPoint usedPoint = userPoint.use(amount);

        pointRepository.save(usedPoint);

        PointHistory history = new PointHistory(
            null,
            id,
            amount,
            PointTransactionType.USE,
            usedPoint.updateMillis()
        );
        pointHistoryRepository.save(history);
        return usedPoint;
    }
}