package kr.hhplus.be.server.application.order;

public record OrderResult(
    Long orderId,
    int totalPrice,
    int discount,
    int finalPrice,
    String orderedAt
) {}
