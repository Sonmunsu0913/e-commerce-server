package kr.hhplus.be.server.infrastructure.point.repository;

import java.util.*;
import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPointHistoryRepository implements PointHistoryRepository {

    private final List<PointHistory> store = new ArrayList<>();
    private long sequence = 1;

    @Override
    public void save(PointHistory history) {
        PointHistory newHistory = new PointHistory(
            sequence++,
            history.userId(),
            history.amount(),
            history.type(),
            history.createdAt()
        );
        store.add(newHistory);
    }

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return store.stream()
            .filter(h -> h.userId() == userId)
            .toList();
    }
}


