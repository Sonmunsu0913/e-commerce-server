package kr.hhplus.be.server.domain.order;

import java.util.*;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void 주문이_정상적으로_생성되면_가격계산이_정확히_이루어진다() {
        // given
        List<OrderItemRequest> items = List.of(
            new OrderItemRequest(1L, "아메리카노", 1500, 2),  // 3000
            new OrderItemRequest(2L, "케이크", 5000, 1)      // 5000
        );
        int discount = 1000;

        // when
        Order order = new Order(1L, 99L, items, discount);

        // then
        assertEquals(8000, order.getTotalPrice());
        assertEquals(1000, order.getDiscount());
        assertEquals(7000, order.getFinalPrice());
    }

    @Test
    void 주문생성시_시간정보가_자동으로_입력된다() {
        // when
        Order order = new Order(1L, 99L, List.of(), 0);

        // then
        assertNotNull(order.getOrderedAt());
    }
}

