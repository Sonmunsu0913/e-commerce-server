package kr.hhplus.be.server.Product.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class TopProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Integer productId;

    @Schema(description = "상품 이름", example = "콜라")
    private String name;

    @Schema(description = "판매 수량", example = "100")
    private Integer salesCount;

    public TopProductResponse(Integer productId, String name, Integer salesCount) {
        this.productId = productId;
        this.name = name;
        this.salesCount = salesCount;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getSalesCount() {
        return salesCount;
    }
}
