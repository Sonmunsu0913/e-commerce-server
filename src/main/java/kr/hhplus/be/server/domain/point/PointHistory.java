package kr.hhplus.be.server.domain.point;

public record PointHistory(
        Long id,
        long userId,
        long amount,
        PointTransactionType type,
        long updateMillis
) {
}
