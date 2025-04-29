package kr.hhplus.be.server.infrastructure.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.hhplus.be.server.domain.product.ProductSale;
import java.time.LocalDate;

@Getter
@Entity
@Table(
    name = "product_sale",
    indexes = {
        @Index(name = "idx_product_id", columnList = "productId")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;
    private LocalDate saleDate;
    private int quantity;

    // id 없이 생성하는 생성자
    public ProductSaleEntity(Long productId, LocalDate saleDate, int quantity) {
        this.productId = productId;
        this.saleDate = saleDate;
        this.quantity = quantity;
    }

    public static ProductSaleEntity from(ProductSale sale) {
        return new ProductSaleEntity(sale.productId(), sale.saleDate(), sale.quantity());
    }

    public ProductSale toDomain() {
        return new ProductSale(productId, saleDate, quantity);
    }
}
