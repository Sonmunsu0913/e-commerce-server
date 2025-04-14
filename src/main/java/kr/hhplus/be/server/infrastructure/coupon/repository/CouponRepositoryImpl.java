package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final JpaCouponRepository jpaRepository;

    public CouponRepositoryImpl(JpaCouponRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return jpaRepository.findById(couponId).map(CouponEntity::toDomain);
    }

    @Override
    public void save(Coupon coupon) {
        jpaRepository.save(CouponEntity.from(coupon));
    }
}