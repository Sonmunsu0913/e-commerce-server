package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.service.ValidatePaymentService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidatePaymentServiceTest {

    private final ValidatePaymentService useCase = new ValidatePaymentService();

    @Test
    void 포인트가_충분하면_예외_없음() {
        // given
        Order order = new Order(1L, 1L, List.of(
            new OrderItemCommand(1L, "라면", 3000, 2)
        ), 0); // 총액: 6000

        int currentPoint = 10000;

        // when & then
        assertDoesNotThrow(() -> useCase.execute(order, currentPoint));
    }

    @Test
    void 포인트가_부족하면_예외발생() {
        // given
        Order order = new Order(1L, 1L, List.of(
            new OrderItemCommand(1L, "김밥", 2000, 3)
        ), 0); // 총액: 6000

        int currentPoint = 4000;

        // when & then
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            useCase.execute(order, currentPoint);
        });

        assertEquals("포인트 부족", ex.getMessage());
    }
}