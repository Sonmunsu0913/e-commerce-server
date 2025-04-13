package kr.hhplus.be.server.domain.product;

import java.time.LocalDate;

public record ProductSale(
    Long productId,
    LocalDate saleDate,
    int quantity
) {

}
