package kr.hhplus.be.server.infrastructure.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.hhplus.be.server.domain.product.Product;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int price;
    private int stock;

    // id를 받는 생성자 (id 포함)
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
