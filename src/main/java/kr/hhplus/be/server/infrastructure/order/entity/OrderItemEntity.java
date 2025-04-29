package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

@Entity
@Table(
    name = "order_item",
    indexes = {
        @Index(name = "idx_product_id", columnList = "productId")
    }
)
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String productName;

    private int price;

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderItemEntity() {}

    public OrderItemEntity(Long productId, String productName, int price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int subtotal() {
        return price * quantity;
    }

    public static OrderItemEntity from(OrderItemCommand request) {
        return new OrderItemEntity(
            request.productId(),
            request.productName(),
            request.price(),
            request.quantity()
        );
    }

    public OrderItemCommand toRequest() {
        return new OrderItemCommand(productId, productName, price, quantity);
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public OrderEntity getOrder() {
        return order;
    }

    // 추가된 getter
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getPrice() { return price; }
    public int getQuantity() { return quantity; }
}
