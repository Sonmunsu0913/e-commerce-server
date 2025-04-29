package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품 이름", example = "콜라")
    private String name;

    @Schema(description = "상품 가격", example = "1500")
    private Integer price;

    @Schema(description = "잔여 수량", example = "10")
    private Integer stock;
}

