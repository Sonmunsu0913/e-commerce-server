package kr.hhplus.be.server.application.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.List;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.order.usecase.CreateOrderUseCase;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    CreateOrderUseCase useCase;

    @Test
    void 주문_생성_정상() {
        Long userId = 1L;
        Long couponId = 101L;
        List<OrderItemRequest> items = List.of(
            new OrderItemRequest(1L, "라면", 3000, 2)
        );

        Order result = useCase.execute(userId, items, couponId);

        assertEquals(userId, result.getUserId());
        assertEquals(6000, result.getTotalPrice());
        assertEquals(1000, result.getDiscount());
        assertEquals(5000, result.getFinalPrice());
        verify(orderRepository).save(any());
    }

    @Test
    void 쿠폰이_null일_경우_할인없음() {
        Long userId = 1L;
        List<OrderItemRequest> items = List.of(
            new OrderItemRequest(2L, "김밥", 2000, 1)
        );

        Order result = useCase.execute(userId, items, null);

        assertEquals(2000, result.getTotalPrice());
        assertEquals(0, result.getDiscount());
    }
}

