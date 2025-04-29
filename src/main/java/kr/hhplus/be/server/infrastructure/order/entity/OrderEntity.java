package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

@Entity
@Table(
        name = "`order`",
        indexes = {
                @Index(name = "idx_user_id", columnList = "userId")
        }
)
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private int totalPrice;
    private int discount;
    private int finalPrice;
    private LocalDateTime orderedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    protected OrderEntity() {}

    public OrderEntity(Long id, Long userId, List<OrderItemEntity> items, int discount) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemEntity::subtotal).sum();
        this.discount = discount;
        this.finalPrice = this.totalPrice - discount;
        this.orderedAt = LocalDateTime.now();

        for (OrderItemEntity item : items) {
            item.setOrder(this); // 연관관계 주입
        }
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
        List<OrderItemCommand> itemCommands = this.items.stream()
            .map(OrderItemEntity::toRequest)
            .collect(Collectors.toList());

        return new Order(id, userId, itemCommands, discount);
    }
}
