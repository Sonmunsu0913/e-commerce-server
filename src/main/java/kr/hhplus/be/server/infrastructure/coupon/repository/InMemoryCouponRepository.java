package kr.hhplus.be.server.infrastructure.coupon.repository;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryCouponRepository implements CouponRepository {

    private final Map<Long, Coupon> couponStorage = new HashMap<>();

    public InMemoryCouponRepository() {
        // 테스트용 쿠폰 1개 등록
        couponStorage.put(101L, new Coupon(101L, "2000원 할인 쿠폰", 2000, 5)); // 총 5장 발급 가능
    }

    @Override
    public Optional<Coupon> findById(Long couponId) {
        return Optional.ofNullable(couponStorage.get(couponId));
    }

    @Override
    public void save(Coupon coupon) {
        couponStorage.put(coupon.getId(), coupon);
    }
}

