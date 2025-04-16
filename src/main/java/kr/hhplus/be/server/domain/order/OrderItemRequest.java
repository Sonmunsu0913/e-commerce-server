package kr.hhplus.be.server.domain.order;

public record OrderItemRequest(
    Long productId,
    String productName,
    int price,
    int quantity
) {
    public int subtotal() {
        return price * quantity;
    }
}

