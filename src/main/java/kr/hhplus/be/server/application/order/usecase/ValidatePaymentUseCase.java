package kr.hhplus.be.server.application.order.usecase;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Payment;
import org.springframework.stereotype.Component;

@Component
public class ValidatePaymentUseCase {

    public void execute(Order order, int currentPoint) {
        new Payment(order).validateEnoughPoint(currentPoint);
    }
}