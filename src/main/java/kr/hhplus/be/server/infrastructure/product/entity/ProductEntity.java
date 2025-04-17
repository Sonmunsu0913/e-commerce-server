package kr.hhplus.be.server.infrastructure.product.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.product.Product;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    private Long id;

    private String name;

    private int price;

    private int stock;

    protected ProductEntity() {
    }

    public ProductEntity(Long id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public static ProductEntity from(Product product) {
        return new ProductEntity(
                product.id(),
                product.name(),
                product.price(),
                product.stock()
        );
    }

    public Product toDomain() {
        return new Product(id, name, price, stock);
    }

}