package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    protected OrderItemEntity() {}

    public OrderItemEntity(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int subtotal() {
        return price * quantity;
    }

    public static OrderItemEntity from(OrderItemCommand request) {
        return new OrderItemEntity(request.productId(), request.quantity());
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
}

