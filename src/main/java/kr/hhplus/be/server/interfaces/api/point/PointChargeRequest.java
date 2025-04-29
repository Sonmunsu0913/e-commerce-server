package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointChargeRequest {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "충전 금액", example = "5000")
    private Integer amount;
}
