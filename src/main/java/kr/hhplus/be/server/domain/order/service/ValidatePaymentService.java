package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Payment;
import org.springframework.stereotype.Service;

@Service
public class ValidatePaymentService {

    public void execute(Order order, int currentPoint) {
        new Payment(order).validateEnoughPoint(currentPoint);
    }
}