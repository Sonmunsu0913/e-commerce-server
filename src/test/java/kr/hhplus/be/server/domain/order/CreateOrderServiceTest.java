package kr.hhplus.be.server.domain.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateOrderServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    CreateOrderService useCase;

    @Test
    void 주문_생성_정상() {
        Long userId = 1L;
        Long couponId = 101L;

        List<OrderItemCommand> items = List.of(
            new OrderItemCommand(1L, "라면", 3000, 2)
        );

        // 💡 productRepository.findWithPessimisticLockById(...) 에 대한 리턴 설정
        Product product = new Product(1L, "라면", 3000, 10);
        when(productRepository.findWithPessimisticLockById(1L)).thenReturn(product);

        Order expectedOrder = new Order(1L, userId, items, 1000);
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        // when
        Order result = useCase.execute(userId, items, couponId);

        // then
        assertEquals(1L, result.getUserId());
        assertEquals(6000, result.getTotalPrice());
        assertEquals(1000, result.getDiscount());
        assertEquals(5000, result.getFinalPrice());
    }

    @Test
    void 쿠폰이_null일_경우_할인없음() {
        Long userId = 1L;
        List<OrderItemCommand> items = List.of(
            new OrderItemCommand(2L, "김밥", 2000, 1)
        );

        // 💡 상품 조회 mock (락 포함)
        Product product = new Product(2L, "김밥", 2000, 5);
        when(productRepository.findWithPessimisticLockById(2L)).thenReturn(product);

        // 💡 주문 저장 결과 mock
        Order fakeOrder = new Order(1L, userId, items, 0);
        when(orderRepository.save(any())).thenReturn(fakeOrder);

        // when
        Order result = useCase.execute(userId, items, null);

        // then
        assertEquals(2000, result.getTotalPrice());
        assertEquals(0, result.getDiscount());
    }
}

