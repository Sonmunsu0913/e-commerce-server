package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void 포인트가_충분하면_검증() {
        // given
        Order order = new Order(1L, 100L,
                List.of(new OrderItemRequest(1L, "커피", 2000, 1)), 0); // finalPrice = 2000

        Payment payment = new Payment(order);
        int currentPoint = 3000;

        // when & then
        assertDoesNotThrow(() -> payment.validateEnoughPoint(currentPoint));
    }

    @Test
    void 포인트가_부족하면_예외() {
        // given
        Order order = new Order(2L, 200L,
                List.of(new OrderItemRequest(1L, "케이크", 4000, 1)), 0); // finalPrice = 4000

        Payment payment = new Payment(order);
        int currentPoint = 3000;

        // when & then
        IllegalStateException e = assertThrows(IllegalStateException.class, () ->
                payment.validateEnoughPoint(currentPoint));

        assertEquals("포인트 부족", e.getMessage());
    }
}

