package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ProductSaleTest {

    @Test
    void 상품_판매_기록_정상_생성() {
        LocalDate today = LocalDate.now();

        ProductSale sale = new ProductSale(1L, today, 5);

        assertEquals(1L, sale.productId());
        assertEquals(today, sale.saleDate());
        assertEquals(5, sale.quantity());
    }
}
