package kr.hhplus.be.server.domain.product;

public class CreateProductCommand {
    private final String name;
    private final int price;
    private final int stock;

    public CreateProductCommand(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    public int stock() {
        return stock;
    }
}
