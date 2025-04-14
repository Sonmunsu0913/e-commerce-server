package kr.hhplus.be.server.application.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.order.usecase.GetOrderUseCase;
import kr.hhplus.be.server.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetOrderUseCaseTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    GetOrderUseCase useCase;

    @Test
    void 주문_조회_정상() {
        Long orderId = 10L;
        Order order = new Order(orderId, 1L, List.of(), 0);

        when(orderRepository.findById(orderId)).thenReturn(order);

        Order result = useCase.execute(orderId);
        assertEquals(orderId, result.getOrderId());
    }

    @Test
    void 주문_없을_경우_예외() {
        when(orderRepository.findById(any())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(999L));
    }
}
