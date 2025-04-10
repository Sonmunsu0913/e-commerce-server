package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final PointService pointService = Mockito.mock(PointService.class);
    private final MockOrderReporter reporter = Mockito.mock(MockOrderReporter.class);

    private final OrderService orderService = new OrderService(orderRepository, pointService, reporter);

    @Test
    void 주문_생성_정상작동() {
        // given
        Long userId = 1L;
        OrderItemRequest item = new OrderItemRequest(1L, "콜라", 1500, 2);  // 1500 * 2 = 3000
        List<OrderItemRequest> items = List.of(item);
        int discount = 1000;  // 할인 금액 1000원

        // when
        Order order = new Order(1000L, userId, items, discount);  // 주문 생성

        // then
        assertNotNull(order);
        assertEquals(3000, order.getTotalPrice());  // 1500 * 2 = 3000
        assertEquals(1000, order.getDiscount());   // 할인 금액 1000
        assertEquals(2000, order.getFinalPrice()); // 3000 - 1000 = 2000
    }

    @Test
    void 주문_응답_정상() {
        // given
        Long userId = 1L;
        OrderItemRequest item = new OrderItemRequest(1L, "콜라", 1500, 2);  // 1500 * 2 = 3000
        List<OrderItemRequest> items = List.of(item);
        int discount = 1000;  // 할인 금액 1000원

        Order order = new Order(1000L, userId, items, discount);  // 주문 생성
        int pointAfterPayment = 2000;  // 예시로 포인트 2000 설정

        // when
        OrderResponse response = order.toResponse(pointAfterPayment);  // OrderResponse로 변환

        // then
        assertNotNull(response);
        assertEquals(1000L, response.getOrderId());        // 주문 ID
        assertEquals(3000, response.getTotalPrice());     // 1500 * 2 = 3000
        assertEquals(1000, response.getDiscount());       // 할인 금액 1000
        assertEquals(2000, response.getFinalPrice());    // 3000 - 1000 = 2000
        assertEquals(pointAfterPayment, response.getPointAfterPayment());  // 포인트
    }

}
