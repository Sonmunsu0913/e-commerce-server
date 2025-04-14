package kr.hhplus.be.server.application.point.usecase;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetPointHistoryUseCase {

    private final PointHistoryRepository pointHistoryRepository;

    public GetPointHistoryUseCase(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public List<PointHistory> execute(long userId) {
        return pointHistoryRepository.findAllByUserId(userId);
    }
}