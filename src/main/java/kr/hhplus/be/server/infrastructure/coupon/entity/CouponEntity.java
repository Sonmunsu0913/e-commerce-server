package kr.hhplus.be.server.infrastructure.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;

@Entity
@Table(name = "coupon")
public class CouponEntity {

    @Id
    private Long id;

    private String name;

    private int discountAmount;

    private int totalQuantity;

    private int issuedCount;

    @Version
    private int version;  // 낙관적 락용 필드

    protected CouponEntity() {}

    public CouponEntity(Long id, String name, int discountAmount, int totalQuantity, int issuedCount) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.issuedCount = issuedCount;
    }

    public static CouponEntity from(Coupon coupon) {
        return new CouponEntity(
                coupon.getId(),
                coupon.getName(),
                coupon.getDiscountAmount(),
                coupon.getTotalQuantity(),
                coupon.getIssuedCount()
        );
    }

    public Coupon toDomain() {
        Coupon coupon = new Coupon(id, name, discountAmount, totalQuantity);
        coupon.setIssuedCount(issuedCount);
        coupon.setVersion(issuedCount);
        return coupon;
    }

    public void increaseIssuedCount() {
        System.out.println("[쿠폰 발급] issuedCount 증가 시도 - 현재 version: " + version);
        this.issuedCount++;
    }

}
