package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PopularProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품 이름", example = "콜라")
    private String productName;

    @Schema(description = "판매 수", example = "150")
    private Long salesCount;

    public PopularProductResponse(Long productId, String productName, Long salesCount) {
        this.productId = productId;
        this.productName = productName;
        this.salesCount = salesCount;
    }

}
