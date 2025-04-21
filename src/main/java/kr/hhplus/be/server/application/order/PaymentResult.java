package kr.hhplus.be.server.application.order;

public record PaymentResult(
    OrderResult order,
    int pointAfterPayment
) {}
