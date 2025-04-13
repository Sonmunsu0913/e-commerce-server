package kr.hhplus.be.server.application.point.service;

import java.util.List;
import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Service;

@Service
public class PointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public PointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    // 사용자의 포인트 이력 조회
    public List<PointHistory> getHistory(long userId) {
        return pointHistoryRepository.findAllByUserId(userId);
    }
}
