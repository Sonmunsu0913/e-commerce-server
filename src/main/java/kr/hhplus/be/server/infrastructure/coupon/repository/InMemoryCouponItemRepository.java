package kr.hhplus.be.server.infrastructure.coupon.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.application.coupon.repository.CouponItemRepository;
import kr.hhplus.be.server.domain.coupon.CouponItem;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryCouponItemRepository implements CouponItemRepository {

    private final List<CouponItem> couponItems = new ArrayList<>();

    // 임시 쿠폰 데이터 추가 (테스트용)
    public InMemoryCouponItemRepository() {
        // 예시: 5개의 쿠폰 추가
        for (long i = 1; i <= 5; i++) {
            couponItems.add(new CouponItem(i));  // CouponItem 객체 생성 (고유 ID는 자동 증가 방식)
        }
    }

    @Override
    public Optional<CouponItem> findFirstByCouponTypeAndIssuedFalse(Long couponId) {
        return couponItems.stream()
            .filter(couponItem -> couponItem.isAvailable() && couponItem.couponTypeId().equals(couponId))
            .findFirst();  // 첫 번째로 발급되지 않은 쿠폰 반환
    }

    @Override
    public void save(CouponItem couponItem) {
        // 발급된 쿠폰을 리스트에 저장
        couponItems.add(couponItem);  // 예시에서는 리스트에 그냥 추가하는 방식
    }
}

