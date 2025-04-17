package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.UserPoint;
import lombok.Getter;

@Getter
public class PointResponse {

    private final Long userId;
    private final long amount;

    public PointResponse(Long userId, long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public static PointResponse from(UserPoint point) {
        return new PointResponse(point.id(), point.point());
    }

}

