package kr.hhplus.be.server.application.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ProductResponse {

    @Schema(description = "상품 ID", example = "1")
    private Integer productId;

    @Schema(description = "상품 이름", example = "콜라")
    private String name;

    @Schema(description = "상품 가격", example = "1500")
    private Integer price;

    @Schema(description = "잔여 수량", example = "10")
    private Integer stock;

    public ProductResponse(Integer productId, String name, Integer price, Integer stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}

