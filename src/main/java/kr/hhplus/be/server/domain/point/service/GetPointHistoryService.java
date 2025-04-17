package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointHistoryRepository;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GetPointHistoryService {

    private final PointHistoryRepository pointHistoryRepository;

    public GetPointHistoryService(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public List<PointHistory> execute(long userId) {
        return pointHistoryRepository.findAllByUserId(userId);
    }
}