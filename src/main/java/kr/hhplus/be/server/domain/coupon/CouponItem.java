package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

@Getter
public class CouponItem {

    private static long idSequence = 1L;

    private final Long id;
    private final Long couponTypeId;
    private boolean issued;

    // 생성자
    public CouponItem(Long couponTypeId) {
        this.id = generateUniqueId();  // 고유 ID 생성
        this.couponTypeId = couponTypeId;
        this.issued = false;           // 기본 발급되지 않음
    }

    // UUID로 고유 ID 생성
    private Long generateUniqueId() {
        return idSequence++;
    }

    public Long couponTypeId() {
        return couponTypeId;
    }

    public boolean isAvailable() {
        return !issued;
    }

    public void markIssued() {
        if (issued) throw new IllegalStateException("이미 발급된 쿠폰입니다.");
        this.issued = true;
    }
}
