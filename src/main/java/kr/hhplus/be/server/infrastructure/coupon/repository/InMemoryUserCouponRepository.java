package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.application.coupon.repository.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserCouponRepository implements UserCouponRepository {

    private final Map<Long, List<UserCoupon>> userCouponStorage = new ConcurrentHashMap<>();

    @Override
    public void save(UserCoupon userCoupon) {
        userCouponStorage
            .computeIfAbsent(userCoupon.userId(), k -> new ArrayList<>())
            .add(userCoupon);
    }

    @Override
    public boolean existsByUserIdAndCouponId(Long userId, Long couponId) {
        return userCouponStorage.getOrDefault(userId, Collections.emptyList())
            .stream()
            .anyMatch(c -> c.couponId().equals(couponId));
    }

    @Override
    public List<UserCoupon> findAllByUserId(Long userId) {
        return new ArrayList<>(userCouponStorage.getOrDefault(userId, Collections.emptyList()));
    }
}
