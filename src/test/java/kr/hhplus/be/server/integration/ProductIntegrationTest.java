package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.infrastructure.product.repository.JpaProductRepository;
import kr.hhplus.be.server.infrastructure.product.repository.JpaProductSaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductSaleRepository productSaleRepository;

    @Autowired
    JpaProductRepository jpaProductRepository;

    @Autowired
    JpaProductSaleRepository jpaProductSaleRepository;

    @BeforeEach
    void setUp() {
        jpaProductSaleRepository.deleteAll();
        jpaProductRepository.deleteAll();
    }

    @Test
    void 상품_목록_조회() throws Exception {
        // 테스트 시작 전 상품 하나 등록
        productRepository.save(new Product(1L, "김밥", 2000, 100));

        // 상품 목록 조회
        mockMvc.perform(get("/api/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("김밥"));
    }

    @Test
    void 인기_상품_조회() throws Exception {
        LocalDate today = LocalDate.now();

        // 상품 10개 삽입
        for (long i = 1; i <= 10; i++) {
            productRepository.save(new Product(i, "상품" + i, 1000 + (int)i * 100, 50));
        }

        // 판매 이력 삽입 (최근 3일 내 1~10번 상품 모두 매출 있음)
        productSaleRepository.save(new ProductSale(8L, today.minusDays(2), 12)); // 인기 2위
        productSaleRepository.save(new ProductSale(9L, today, 7));                           // 인기 5위
        productSaleRepository.save(new ProductSale(7L, today, 2));                           // 인기 9위
        productSaleRepository.save(new ProductSale(3L, today.minusDays(2), 10)); // 인기 3위
        productSaleRepository.save(new ProductSale(2L, today.minusDays(1), 20)); // 인기 1위
        productSaleRepository.save(new ProductSale(6L, today.minusDays(1), 8));  // 인기 4위
        productSaleRepository.save(new ProductSale(5L, today, 1));                           // 인기 10위
        productSaleRepository.save(new ProductSale(1L, today, 5));                           // 인기 6위
        productSaleRepository.save(new ProductSale(4L, today.minusDays(1), 3));  // 인기 8위
        productSaleRepository.save(new ProductSale(10L, today.minusDays(1), 4)); // 인기 7위

        mockMvc.perform(get("/api/product/sale/statistics/popular?range=3d")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(2L)) // 가장 많이 팔린 상품 검증 - 1위
                .andExpect(jsonPath("$[1].productId").value(8L)) // 가장 많이 팔린 상품 검증 - 2위
                .andExpect(jsonPath("$[2].productId").value(3L)) // 가장 많이 팔린 상품 검증 - 3위
                .andExpect(jsonPath("$[3].productId").value(6L)) // 가장 많이 팔린 상품 검증 - 4위
                .andExpect(jsonPath("$[4].productId").value(9L)); // 가장 많이 팔린 상품 검증 - 5위
    }
}
