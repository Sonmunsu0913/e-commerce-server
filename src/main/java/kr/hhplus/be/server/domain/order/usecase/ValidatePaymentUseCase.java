package kr.hhplus.be.server.domain.order.usecase;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Payment;
import org.springframework.stereotype.Service;

@Service
public class ValidatePaymentUseCase {

    public void execute(Order order, int currentPoint) {
        new Payment(order).validateEnoughPoint(currentPoint);
    }
}