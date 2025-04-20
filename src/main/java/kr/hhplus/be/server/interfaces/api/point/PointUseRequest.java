package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.media.Schema;

public class PointUseRequest {
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "사용 금액", example = "3000")
    private Integer amount;

    public Long getUserId() { return userId; }
    public Integer getAmount() { return amount; }
}

