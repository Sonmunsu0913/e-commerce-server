package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

@Getter
public class Coupon {

    private final Long id;
    private final String name;
    private final int discountAmount;
    private final int totalQuantity;
    private int issuedCount;

    public Coupon(Long id, String name, int discountAmount, int totalQuantity) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.issuedCount = 0;
    }

    public void checkAndIncreaseIssuedCount() {
        if (issuedCount >= totalQuantity) {
            throw new IllegalStateException("모든 쿠폰이 소진되었습니다.");
        }
        issuedCount++;
    }

    public boolean canIssue() {
        return issuedCount < totalQuantity;
    }
}
