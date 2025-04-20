package kr.hhplus.be.server.infrastructure.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.ProductSale;

import java.time.LocalDate;

@Entity
@Table(
        name = "product_sale",
        indexes = {
                @Index(name = "idx_product_id", columnList = "productId")
        }
)
public class ProductSaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private LocalDate saleDate;

    private int quantity;

    protected ProductSaleEntity() {
    }

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

