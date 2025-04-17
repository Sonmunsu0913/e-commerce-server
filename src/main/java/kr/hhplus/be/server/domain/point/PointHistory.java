package kr.hhplus.be.server.domain.point;

import java.time.LocalDateTime;

public record PointHistory(
        Long id,
        long userId,
        long amount,
        PointTransactionType type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
