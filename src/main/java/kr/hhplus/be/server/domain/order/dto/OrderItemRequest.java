package kr.hhplus.be.server.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class OrderItemRequest {

    private Long productId;
    private String productName;
    private int price;
    private int quantity;

    public OrderItemRequest(Long productId, String productName, int price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int subtotal() {
        return price * quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

}


