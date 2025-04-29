package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TopProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Integer productId;

    @Schema(description = "상품 이름", example = "콜라")
    private String name;

    @Schema(description = "판매 수량", example = "100")
    private Integer salesCount;
}
