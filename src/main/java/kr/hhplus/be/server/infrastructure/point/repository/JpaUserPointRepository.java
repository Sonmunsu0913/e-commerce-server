package kr.hhplus.be.server.infrastructure.point.repository;

import kr.hhplus.be.server.infrastructure.point.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserPointRepository extends JpaRepository<UserPointEntity, Long> {
}