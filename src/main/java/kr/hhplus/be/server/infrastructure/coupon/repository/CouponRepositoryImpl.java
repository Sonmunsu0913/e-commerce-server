package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.infrastructure.coupon.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final JpaCouponRepository jpaRepository;

    public CouponRepositoryImpl(JpaCouponRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Coupon> findAllCoupons() {
        return jpaRepository.findAllEntities()
                .stream()
                .map(CouponEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return jpaRepository.findById(couponId).map(CouponEntity::toDomain);
    }

    @Override
    public void save(Coupon coupon) {
        jpaRepository.save(CouponEntity.from(coupon));
    }

    @Override
    public Coupon findWithPessimisticLockById(Long couponId) {
        return jpaRepository.findWithPessimisticLockById(couponId)
                .toDomain(); // 혹은 .toModel() 등, 엔티티 → 도메인 변환
    }

    @Override
    public Coupon findWithOptimisticLockById(Long couponId) {
        return jpaRepository.findWithOptimisticLockById(couponId)
                .toDomain();
    }

    @Override
    public boolean existsById(Long couponId) {
        return jpaRepository.existsById(couponId);
    }
}