package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PointHistoryResponse {

    private final String type;
    private final long amount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static PointHistoryResponse from(PointHistory history) {
        return PointHistoryResponse.builder()
            .type(history.type().name())
            .amount(history.amount())
            .createdAt(history.createdAt())
            .updatedAt(history.updatedAt())
            .build();
    }
}
