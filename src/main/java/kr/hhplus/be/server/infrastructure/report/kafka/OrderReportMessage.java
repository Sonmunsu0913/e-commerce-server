package kr.hhplus.be.server.infrastructure.report.kafka;

import java.time.LocalDateTime;

public record OrderReportMessage(
        Long orderId,
        int totalPrice,
        int discount,
        int finalPrice,
        LocalDateTime orderedAt
) {}
