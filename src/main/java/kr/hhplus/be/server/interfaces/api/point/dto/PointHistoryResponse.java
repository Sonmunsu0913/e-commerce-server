package kr.hhplus.be.server.interfaces.api.point.dto;

import kr.hhplus.be.server.domain.point.PointHistory;

public class PointHistoryResponse {
    private final String type;
    private final long amount;
    private final String timestamp;

    public PointHistoryResponse(String type, long amount, String timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public static PointHistoryResponse from(PointHistory history) {
        return new PointHistoryResponse(
                history.type().name(),
                history.amount(),
                String.valueOf(history.updateMillis())
        );
    }
}
