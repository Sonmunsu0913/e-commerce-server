package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

public record CreateOrderCommand(
    Long userId,
    List<OrderItemCommand> items,
    Long couponId
) {}
