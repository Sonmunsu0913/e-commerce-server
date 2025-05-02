package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

@Getter
public class Coupon {

    private final Long id;               // 쿠폰 고유 ID
    private final String name;           // 쿠폰 이름
    private final int discountAmount;    // 할인 금액
    private final int totalQuantity;     // 전체 발급 가능 수량
    private int issuedCount;             // 현재까지 발급된 수량

    public Coupon(Long id, String name, int discountAmount, int totalQuantity) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.issuedCount = 0;
    }

    /**
     * 발급 처리
     * - 발급 가능 여부를 확인 후, issuedCount 증가
     */
    public void issue() {
        if (!canIssue()) {
            throw new IllegalStateException("발급 가능한 쿠폰이 없습니다.");
        }
        issuedCount++;
    }

    /**
     * 발급 가능 여부 확인
     * - issuedCount < totalQuantity 이면 발급 가능
     */
    public boolean canIssue() {
        return issuedCount < totalQuantity;
    }

    public void setIssuedCount(int issuedCount) {
        this.issuedCount = issuedCount;
    }

}

