package kr.hhplus.be.server.application.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PointResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "현재 잔액", example = "60000")
    private Integer point;

    public PointResponse(Long userId, Integer point) {
        this.userId = userId;
        this.point = point;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getPoint() {
        return point;
    }
}

