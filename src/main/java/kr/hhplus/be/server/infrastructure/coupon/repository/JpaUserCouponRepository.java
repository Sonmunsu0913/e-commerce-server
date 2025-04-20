package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.infrastructure.coupon.entity.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserCouponRepository extends JpaRepository<UserCouponEntity, Long> {
    List<UserCouponEntity> findAllByUserId(Long userId);
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
