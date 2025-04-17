package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {

    @Query("SELECT c FROM CouponEntity c")
    List<CouponEntity> findAllEntities();  // or just use findAll()
}
