package kr.hhplus.be.server.infrastructure.point.repository;

import kr.hhplus.be.server.infrastructure.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
    List<PointHistoryEntity> findAllByUserId(long userId);
}

