package kr.hhplus.be.server.interfaces.api.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PointChargeRequest {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "충전 금액", example = "5000")
    private Integer amount;

    public Long getUserId() {
        return userId;
    }

    public Integer getAmount() {
        return amount;
    }
}
