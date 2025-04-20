package kr.hhplus.be.server.infrastructure.point.repository;

import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.infrastructure.point.entity.UserPointEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository {

    private final JpaUserPointRepository jpaRepo;

    public PointRepositoryImpl(JpaUserPointRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public UserPoint findById(long id) {
        return jpaRepo.findById(id)
                .map(UserPointEntity::toDomain)
                .orElse(UserPoint.empty(id));
    }

    @Override
    public void save(UserPoint point) {
        jpaRepo.save(UserPointEntity.from(point));
    }
}

