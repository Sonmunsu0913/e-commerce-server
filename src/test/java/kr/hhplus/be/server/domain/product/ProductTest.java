package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void 재고가_충분할_때_isAvailable_true() {
        Product product = new Product(1L, "콜라", 1500, 10);

        assertTrue(product.isAvailable(5));
    }

    @Test
    void 재고가_부족할_때_isAvailable_false() {
        Product product = new Product(1L, "콜라", 1500, 3);

        assertFalse(product.isAvailable(4));
    }

    @Test
    void 재고가_충분할_때_reduceStock_정상_동작() {
        Product product = new Product(1L, "사이다", 1400, 10);

        Product updated = product.reduceStock(3);

        assertEquals(7, updated.stock());
    }

    @Test
    void 재고가_부족할_때_reduceStock_예외() {
        Product product = new Product(1L, "사이다", 1400, 2);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            product.reduceStock(5);
        });

        assertEquals("재고가 부족합니다.", exception.getMessage());
    }
}
