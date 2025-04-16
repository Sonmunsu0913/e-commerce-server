package kr.hhplus.be.server.infrastructure.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;

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

    protected OrderEntity() {}

    public OrderEntity(Long id, Long userId, List<OrderItemEntity> items, int discount) {
        this.id = id;
        this.userId = userId;
        this.totalPrice = items.stream().mapToInt(OrderItemEntity::subtotal).sum();
        this.discount = discount;
        this.finalPrice = this.totalPrice - discount;
        this.orderedAt = LocalDateTime.now().toString();

        for (OrderItemEntity item : items) {
            item.setOrder(this); // 연관관계 설정
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
        return new Order(id, userId, null, discount); // items는 별도로 조회해야 함
    }
}

