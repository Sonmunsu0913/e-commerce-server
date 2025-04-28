package kr.hhplus.be.server.infrastructure.coupon.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.UserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.infrastructure.coupon.entity.UserCouponEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

    private final JpaUserCouponRepository jpaRepository;

    public UserCouponRepositoryImpl(JpaUserCouponRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(UserCoupon userCoupon) {
        jpaRepository.save(UserCouponEntity.from(userCoupon));
    }

    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        return jpaRepository.existsByUserIdAndCouponId(userId, couponId);
    }

    @Override
    public List<UserCoupon> findAllByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream()
                .map(UserCouponEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId) {
        return jpaRepository.findByUserIdAndCouponId(userId, couponId)
            .map(UserCouponEntity::toDomain);
    }
}
