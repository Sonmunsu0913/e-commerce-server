package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.*;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import lombok.Getter;

@Getter
public class Order {
    private final Long orderId;
    private final Long userId;
    private final List<OrderItemRequest> items;
    private final int totalPrice;
    private final int discount;
    private final int finalPrice;
    private final String orderedAt;

    public Order(Long orderId, Long userId, List<OrderItemRequest> items, int discount) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemRequest::subtotal).sum();
        this.discount = discount;
        this.finalPrice = totalPrice - discount;
        this.orderedAt = LocalDateTime.now().toString();
    }

    public OrderResponse toResponse(int pointAfterPayment) {
        return new OrderResponse(
            orderId, totalPrice, discount, finalPrice, pointAfterPayment, orderedAt
        );
    }
}

