package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItemRequest;

@Entity
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    private String productName;

    private int price;

    private int quantity;

    protected OrderItemEntity() {}

    public OrderItemEntity(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int subtotal() {
        return price * quantity;
    }

    public static OrderItemEntity from(OrderItemRequest request) {
        return new OrderItemEntity(request.productId(), request.quantity());
    }

    public OrderItemRequest toRequest() {
        return new OrderItemRequest(productId, productName, price, quantity);
    }
}

