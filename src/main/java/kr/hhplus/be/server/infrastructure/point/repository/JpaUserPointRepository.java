package kr.hhplus.be.server.infrastructure.point.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infrastructure.point.entity.UserPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserPointRepository extends JpaRepository<UserPointEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM UserPointEntity u WHERE u.id = :userId")
    Optional<UserPointEntity> findWithPessimisticLockById(@Param("userId") Long userId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT u FROM UserPointEntity u WHERE u.id = :userId")
    UserPointEntity findWithOptimisticLockById(@Param("userId") Long userId);
}