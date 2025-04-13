package kr.hhplus.be.server.infrastructure.point.repository;

import java.util.HashMap;
import java.util.Map;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryPointRepository implements PointRepository {

    private final Map<Long, UserPoint> store = new HashMap<>();

    @Override
    public UserPoint findById(long id) {
        return store.getOrDefault(id, UserPoint.empty(id));
    }

    @Override
    public void save(UserPoint point) {
        store.put(point.id(), point);
    }
}
