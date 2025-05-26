package kr.hhplus.be.server.domain.order.event;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

public record OrderRequestedEventPayload(
    Long orderId,
    Long userId,
    int finalPrice,
    List<OrderItemCommand> items,
    Long couponId
) {}
