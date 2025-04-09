package kr.hhplus.be.server.domain.product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductSaleStatistics {

    private final Map<Long, Long> totalSalesPerProduct;

    private ProductSaleStatistics(Map<Long, Long> totalSalesPerProduct) {
        this.totalSalesPerProduct = totalSalesPerProduct;
    }

    public static ProductSaleStatistics of(List<ProductSale> sales) {
        Map<Long, Long> map = sales.stream()
            .collect(Collectors.groupingBy(
                ProductSale::productId,
                Collectors.summingLong(ProductSale::quantity)
            ));
        return new ProductSaleStatistics(map);
    }

    public List<Map.Entry<Long, Long>> topN(int limit) {
        return totalSalesPerProduct.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
            .limit(limit)
            .toList();
    }

    public List<Long> extractProductIds(List<Map.Entry<Long, Long>> entries) {
        return entries.stream().map(Map.Entry::getKey).toList();
    }

    // Optional: getter
    public Map<Long, Long> getTotalSalesPerProduct() {
        return totalSalesPerProduct;
    }
}

