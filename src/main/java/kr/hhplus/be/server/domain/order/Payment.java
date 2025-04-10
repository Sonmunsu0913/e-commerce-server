package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.interfaces.api.order.dto.PaymentResultResponse;

import java.time.LocalDateTime;

public class Payment {

    private final Order order;

    public Payment(Order order) {
        this.order = order;
    }

    public void validateEnoughPoint(int currentPoint) {
        if (currentPoint < order.getFinalPrice()) {
            throw new IllegalStateException("포인트 부족");
        }
    }
}
