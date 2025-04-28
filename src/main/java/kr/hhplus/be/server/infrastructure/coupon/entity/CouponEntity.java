package kr.hhplus.be.server.infrastructure.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CouponEntity {

    @Id
    private Long id;

    private String name;

    private int discountAmount;

    private int totalQuantity;

    private int issuedCount;

    @Version
    private int version;  // 낙관적 락용 필드

    public static CouponEntity from(Coupon coupon) {
        return new CouponEntity(
            coupon.getId(),
            coupon.getName(),
            coupon.getDiscountAmount(),
            coupon.getTotalQuantity(),
            coupon.getIssuedCount(),
            0
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
