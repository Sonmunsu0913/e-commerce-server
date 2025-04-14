package kr.hhplus.be.server.infrastructure.point.repository;

import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.infrastructure.point.entity.PointHistoryEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

    private final JpaPointHistoryRepository jpaRepo;

    public PointHistoryRepositoryImpl(JpaPointHistoryRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public void save(PointHistory history) {
        jpaRepo.save(PointHistoryEntity.from(history));
    }

    @Override
    public List<PointHistory> findAllByUserId(long userId) {
        return jpaRepo.findAllByUserId(userId).stream()
                .map(e -> e.toDomain(e.getId()))
                .toList();
    }
}


