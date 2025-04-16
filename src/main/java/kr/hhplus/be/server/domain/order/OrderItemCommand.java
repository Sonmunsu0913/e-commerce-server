package kr.hhplus.be.server.domain.order;

public record OrderItemCommand(
    Long productId,
    String productName,
    int price,
    int quantity
) {
    public int subtotal() {
        return price * quantity;
    }
}

