package kr.hhplus.be.server.infrastructure.coupon.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {

    @Query("SELECT c FROM CouponEntity c")
    List<CouponEntity> findAllEntities();  // or just use findAll()

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM CouponEntity c WHERE c.id = :couponId")
    CouponEntity findWithPessimisticLockById(@Param("couponId") Long couponId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT c FROM CouponEntity c WHERE c.id = :couponId")
    CouponEntity findWithOptimisticLockById(@Param("couponId") Long couponId);
}
