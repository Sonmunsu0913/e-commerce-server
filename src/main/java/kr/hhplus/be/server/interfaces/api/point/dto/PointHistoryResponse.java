package kr.hhplus.be.server.interfaces.api.point.dto;

import kr.hhplus.be.server.domain.point.PointHistory;

public class PointHistoryResponse {
    private final String type;
    private final long amount;
    private final String createdAt;

    public PointHistoryResponse(String type, long amount, String createdAt) {
        this.type = type;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public static PointHistoryResponse from(PointHistory history) {
        return new PointHistoryResponse(
                history.type().name(),
                history.amount(),
                history.createdAt().toString()
        );
    }
}
