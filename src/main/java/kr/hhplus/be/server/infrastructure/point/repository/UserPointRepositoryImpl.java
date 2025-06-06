package kr.hhplus.be.server.infrastructure.point.repository;

import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.infrastructure.point.entity.UserPointEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository {

    private final JpaUserPointRepository jpaRepo;

    public UserPointRepositoryImpl(JpaUserPointRepository jpaRepo) {
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

    @Override
    public UserPoint findWithPessimisticLockById(long userId) {
        return jpaRepo.findWithPessimisticLockById(userId)
                .map(UserPointEntity::toDomain)
                .orElse(UserPoint.empty(userId)); // 혹은 예외 던지기
    }

    @Override
    public UserPoint findWithOptimisticLockById(long userId) {
        return jpaRepo.findWithOptimisticLockById(userId)
            .toDomain();
    }
}

