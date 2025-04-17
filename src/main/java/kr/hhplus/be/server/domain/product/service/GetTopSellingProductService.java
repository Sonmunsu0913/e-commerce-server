package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSaleRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.ProductSaleStatistics;
import kr.hhplus.be.server.interfaces.api.product.PopularProductResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * 인기 상품 조회 유스케이스 (최근 n일 기준)
 */
@Service
public class GetTopSellingProductService {

    private final ProductSaleRepository productSaleRepository;
    private final ProductRepository productRepository;

    public GetTopSellingProductService(ProductSaleRepository productSaleRepository, ProductRepository productRepository) {
        this.productSaleRepository = productSaleRepository;
        this.productRepository = productRepository;
    }

    public List<PopularProductResponse> execute(String range) {
        int days = parseRangeToDays(range);
        LocalDate from = LocalDate.now().minusDays(days);

        // 1. 최근 판매 내역 조회
        List<ProductSale> sales = productSaleRepository.findSalesAfter(from);

        // 2. 판매 통계 집계
        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);
        List<Map.Entry<Long, Long>> topEntries = statistics.topN(5);

        // 3. 상품 이름 조회
        Map<Long, String> productNames = productRepository.findAllByIdIn(statistics.extractProductIds(topEntries))
                .stream().collect(Collectors.toMap(Product::id, Product::name));

        // 4. 응답 객체로 변환
        return topEntries.stream()
                .map(entry -> new PopularProductResponse(
                        entry.getKey(),
                        productNames.getOrDefault(entry.getKey(), "알 수 없음"),
                        entry.getValue()
                )).toList();
    }

    // "d" → int 로 변환
    private int parseRangeToDays(String range) {
        if (range.endsWith("d")) {
            try {
                return Integer.parseInt(range.replace("d", ""));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("range 형식이 잘못되었습니다. 예: 3d");
            }
        }
        throw new IllegalArgumentException("지원하지 않는 range 단위입니다. 예: 3d");
    }
}
