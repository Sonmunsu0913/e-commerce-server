package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private Long orderId;

    private Long userId;

    private int totalPrice;
    private int discount;
    private int finalPrice;
    private String orderedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    protected OrderEntity() {}

    public OrderEntity(Long orderId, Long userId, List<OrderItemEntity> items, int discount) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemEntity::subtotal).sum();
        this.discount = discount;
        this.finalPrice = this.totalPrice - discount;
        this.orderedAt = LocalDateTime.now().toString();
    }

    public static OrderEntity from(Order order) {
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(OrderItemEntity::from)
                .collect(Collectors.toList());
        return new OrderEntity(
                order.getOrderId(),
                order.getUserId(),
                itemEntities,
                order.getDiscount()
        );
    }

    public Order toDomain() {
        List<OrderItemRequest> itemRequests = items.stream()
                .map(OrderItemEntity::toRequest)
                .collect(Collectors.toList());
        return new Order(orderId, userId, itemRequests, discount);
    }
}

