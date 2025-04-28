package kr.hhplus.be.server.interfaces.api.product;

import kr.hhplus.be.server.domain.product.CreateProductCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

    private String name;
    private int price;
    private int stock;

    // 💡 CreateProductCommand로 변환
    public CreateProductCommand toCommand() {
        return new CreateProductCommand(name, price, stock);
    }
}
