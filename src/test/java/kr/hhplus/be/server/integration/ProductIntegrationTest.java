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
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Autowired
    CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        jpaProductSaleRepository.deleteAll();
        jpaProductRepository.deleteAll();
        cacheManager.getCache("POPULAR_PRODUCTS").clear();
    }

    @Test
    void 상품_목록_조회() throws Exception {
        // 테스트 시작 전 상품 하나 등록
        productRepository.save(new Product(null, "김밥", 2000, 100));

        // 상품 목록 조회
        mockMvc.perform(get("/api/product")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("김밥"));
    }

    @Test
    void 인기_상품_조회() throws Exception {
        LocalDate today = LocalDate.now();

        System.out.println("📦 상품 저장 시작");
        Map<String, Long> productNameToId = new HashMap<>();
        // 상품 10개 삽입 및 ID 추적
        for (long i = 1; i <= 10; i++) {
            Product saved = productRepository.save(new Product(null, "상품" + i, 1000 + (int) i * 100, 50));
            productNameToId.put("상품" + i, saved.id());
            System.out.println("저장된 상품: " + saved.name() + ", ID = " + saved.id());
        }

        // ID 확인용 역순 Map
        Map<Long, String> idToName = productNameToId.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

        // 판매 이력 삽입
        System.out.println("🧾 판매 이력 등록");
        long[][] sales = {
                {8, 2, 12}, {9, 0, 7}, {7, 0, 2}, {3, 2, 10}, {2, 1, 20},
                {6, 1, 8}, {5, 0, 1}, {1, 0, 5}, {4, 1, 3}, {10, 1, 4}
        };

        for (long[] entry : sales) {
            long productNo = entry[0];
            LocalDate date = today.minusDays(entry[1]);
            int quantity = (int) entry[2];
            Long id = productNameToId.get("상품" + productNo);
            productSaleRepository.save(new ProductSale(id, date, quantity));
            System.out.println("🛒 판매 등록 - 상품: 상품" + productNo + " (ID=" + id + "), 날짜=" + date + ", 수량=" + quantity);
        }

        System.out.println("📡 인기 상품 조회 API 호출");

        String response = mockMvc.perform(get("/api/product/sale/statistics/ranking/db?range=3d")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println("응답 JSON: " + response);

        mockMvc.perform(get("/api/product/sale/statistics/ranking/db?range=3d")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(productNameToId.get("상품2")))  // 1위
                .andExpect(jsonPath("$[1].productId").value(productNameToId.get("상품8")))  // 2위
                .andExpect(jsonPath("$[2].productId").value(productNameToId.get("상품3")))  // 3위
                .andExpect(jsonPath("$[3].productId").value(productNameToId.get("상품6")))  // 4위
                .andExpect(jsonPath("$[4].productId").value(productNameToId.get("상품9"))); // 5위
    }

}
