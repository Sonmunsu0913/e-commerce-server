package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.UserPoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PointResponse {

    private final Long userId;
    private final long amount;

    public static PointResponse from(UserPoint point) {
        return new PointResponse(point.id(), point.point());
    }
}

