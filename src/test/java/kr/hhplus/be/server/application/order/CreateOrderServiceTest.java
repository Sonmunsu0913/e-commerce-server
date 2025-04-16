package kr.hhplus.be.server.application.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItemRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    CreateOrderService useCase;

    @Test
    void 주문_생성_정상() {
        Long userId = 1L;
        Long couponId = 101L;
        List<OrderItemRequest> items = List.of(
                new OrderItemRequest(1L, "라면", 3000, 2)
        );

        // given: save 시 결과로 반환할 Order 객체 정의
        Order fakeOrder = new Order(1L, userId, items, 1000);
        when(orderRepository.save(any())).thenReturn(fakeOrder);

        // when
        Order result = useCase.execute(userId, items, couponId);

        // then
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

        Order fakeOrder = new Order(1L, userId, items, 0);
        when(orderRepository.save(any())).thenReturn(fakeOrder);

        Order result = useCase.execute(userId, items, null);

        assertEquals(2000, result.getTotalPrice());
        assertEquals(0, result.getDiscount());
    }
}

