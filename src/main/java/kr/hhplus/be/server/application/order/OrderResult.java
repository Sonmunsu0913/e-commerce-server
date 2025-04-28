package kr.hhplus.be.server.application.order;

import java.time.LocalDateTime;

public record OrderResult(
    Long orderId,
    int totalPrice,
    int discount,
    int finalPrice,
    LocalDateTime orderedAt,
    int pointAfterPayment
) {}
