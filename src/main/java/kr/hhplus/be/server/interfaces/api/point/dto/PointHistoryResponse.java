package kr.hhplus.be.server.interfaces.api.point.dto;

import kr.hhplus.be.server.domain.point.PointHistory;
import java.time.LocalDateTime;

public class PointHistoryResponse {
    private final String type;
    private final long amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public PointHistoryResponse(String type, long amount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PointHistoryResponse from(PointHistory history) {
        return new PointHistoryResponse(
                history.type().name(),
                history.amount(),
                history.createdAt(),
                history.updatedAt()
        );
    }
}
