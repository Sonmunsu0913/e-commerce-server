package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.ProductSaleStatistics;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSaleStatisticsService {

    private final ProductSaleService productSaleService;
    private final ProductRepository productRepository;

    public ProductSaleStatisticsService(ProductSaleService productSaleService, ProductRepository productRepository) {
        this.productSaleService = productSaleService;
        this.productRepository = productRepository;
    }

    public List<PopularProductResponse> getTopSellingProducts(String range) {
        int days = parseRangeToDays(range);
        LocalDate from = LocalDate.now().minusDays(days);

        List<ProductSale> sales = productSaleService.findSalesAfter(from);
        ProductSaleStatistics statistics = ProductSaleStatistics.of(sales);

        List<Map.Entry<Long, Long>> topEntries = statistics.topN(5);
        Map<Long, String> productNames = productRepository.findAllByIdIn(statistics.extractProductIds(topEntries)).stream()
            .collect(Collectors.toMap(Product::id, Product::name));

        return topEntries.stream()
            .map(entry -> new PopularProductResponse(
                entry.getKey(),
                productNames.getOrDefault(entry.getKey(), "알 수 없음"),
                entry.getValue()
            ))
            .toList();
    }

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


