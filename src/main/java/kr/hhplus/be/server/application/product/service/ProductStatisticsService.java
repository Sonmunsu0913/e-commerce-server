package kr.hhplus.be.server.application.product.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.stereotype.Service;

@Service
public class ProductStatisticsService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public ProductStatisticsService(ProductSaleRepository productSaleRepository, ProductRepository productRepository) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
    }

    public List<PopularProductResponse> getPopularProducts(String range) {
        int days = parseDays(range);
        LocalDate fromDate = LocalDate.now().minusDays(days);

        List<ProductSale> sales = productSaleRepository.findSalesAfter(fromDate);

        Map<Long, Long> saleCountPerProduct = sales.stream()
            .collect(Collectors.groupingBy(ProductSale::productId, Collectors.summingLong(ProductSale::quantity)));

        // 정렬하고 상위 5개 추출
        return saleCountPerProduct.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(5)
            .map(entry -> {
                Long productId = entry.getKey();
                String name = productRepository.findById(productId).get().name(); // 또는 캐싱
                return new PopularProductResponse(productId, name, entry.getValue());
            })
            .toList();
    }


    private int parseDays(String range) {
        try {
            if (range.endsWith("d")) {
                return Integer.parseInt(range.replace("d", ""));
            }
            throw new IllegalArgumentException("지원하지 않는 range 형식입니다. ex) 3d");
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("range 파라미터 형식이 잘못되었습니다.");
        }
    }
}

