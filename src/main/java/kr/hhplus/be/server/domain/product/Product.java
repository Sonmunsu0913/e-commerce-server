package kr.hhplus.be.server.domain.product;

public record Product(
    Long id,
    String name,
    int price,
    int stock
) {
    public boolean isAvailable(int quantity) {
        return stock >= quantity;
    }

    public Product reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }
        return new Product(id, name, price, stock - quantity);
    }
}
