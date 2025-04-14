package kr.hhplus.be.server.domain.order;

import java.util.*;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void 주문_정상_생성_및_필드_계산() {
        Long orderId = 1001L;
        Long userId = 1L;
        List<OrderItemRequest> items = List.of(
            new OrderItemRequest(1L, "떡볶이", 5000, 2),
            new OrderItemRequest(2L, "콜라", 1500, 1)
        );
        int discount = 1000;

        Order order = new Order(orderId, userId, items, discount);

        assertEquals(11500, order.getTotalPrice());
        assertEquals(1000, order.getDiscount());
        assertEquals(10500, order.getFinalPrice());
        assertNotNull(order.getOrderedAt());
    }

    @Test
    void 주문응답_DTO_변환_정상() {
        Order order = new Order(1002L, 1L,
            List.of(new OrderItemRequest(1L, "김밥", 3000, 2)),
            500
        );

        OrderResponse response = order.toResponse(7000);

        assertEquals(1002L, response.getOrderId());
        assertEquals(6000, response.getTotalPrice());
        assertEquals(500, response.getDiscount());
        assertEquals(5500, response.getFinalPrice());
        assertEquals(7000, response.getPointAfterPayment());
    }
}


