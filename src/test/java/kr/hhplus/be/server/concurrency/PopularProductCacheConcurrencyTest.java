package kr.hhplus.be.server.concurrency;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PopularProductCacheConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSaleRepository productSaleRepository;

    @Test
    void 인기_상품_조회_동시성_캐시_테스트() throws Exception {
        // given
        LocalDate today = LocalDate.now();

        Product product = new Product(null, "동시성테스트상품", 1000, 50);
        productRepository.save(product);

        // DB에서 id를 조회해야 하는 경우 다시 불러오기 (Optional)
        Long productId = productRepository.findAll().stream()
                .filter(p -> p.name().equals("동시성테스트상품"))
                .findFirst()
                .orElseThrow()
                .id();

        productSaleRepository.save(new ProductSale(productId, today, 20));

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int threadNum = i; // 로그에 쓰기 위해
            executor.submit(() -> {
                try {
                    System.out.println("[Thread-" + threadNum + "] 요청 시작");
                    mockMvc.perform(get("/api/product/sale/statistics/popular?range=3d")
                                    .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
                    System.out.println("[Thread-" + threadNum + "] 요청 성공");
                } catch (Exception e) {
                    System.out.println("[Thread-" + threadNum + "] 요청 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
    }
}
