package kr.hhplus.be.server.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateProductCommand {

    private final String name;
    private final int price;
    private final int stock;
}
