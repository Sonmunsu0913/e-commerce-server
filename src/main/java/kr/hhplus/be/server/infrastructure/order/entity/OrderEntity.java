package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "`order`")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int totalPrice;
    private int discount;
    private int finalPrice;
    private String orderedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items;

    protected OrderEntity() {}

    public OrderEntity(Long id, Long userId, List<OrderItemEntity> items, int discount) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemEntity::subtotal).sum();
        this.discount = discount;
        this.finalPrice = this.totalPrice - discount;
        this.orderedAt = LocalDateTime.now().toString();
    }

    public Long getId() {
        return id;
    }

    public static OrderEntity from(Order order) {
        List<OrderItemEntity> itemEntities = order.getItems().stream()
                .map(OrderItemEntity::from)
                .collect(Collectors.toList());
        return new OrderEntity(
                order.getId(),
                order.getUserId(),
                itemEntities,
                order.getDiscount()
        );
    }

    public Order toDomain() {
        List<OrderItemRequest> itemRequests = items.stream()
                .map(OrderItemEntity::toRequest)
                .collect(Collectors.toList());
        return new Order(id, userId, itemRequests, discount);
    }
}

