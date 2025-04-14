package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCouponRepository extends JpaRepository<CouponEntity, Long> {
}
