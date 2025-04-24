package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.CreateProductCommand;

public class ProductCreateRequest {
    private String name;
    private int price;
    private int stock;

    public ProductCreateRequest() {}

    public ProductCreateRequest(String name, int price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // 💡 CreateProductCommand로 변환
    public CreateProductCommand toCommand() {
        return new CreateProductCommand(name, price, stock);
    }
}
