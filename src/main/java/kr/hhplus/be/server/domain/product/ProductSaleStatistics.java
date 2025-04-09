package kr.hhplus.be.server.domain.product;

import java.util.*;
import java.util.stream.Collectors;

public class ProductSaleStatistics {

    public static Map<Long, Long> aggregateSales(List<ProductSale> sales) {
        return sales.stream()
                .collect(Collectors.groupingBy(
                        ProductSale::productId,
                        Collectors.summingLong(ProductSale::quantity)
                ));
    }

    public static List<Map.Entry<Long, Long>> topN(Map<Long, Long> saleMap, int n) {
        return saleMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(n)
                .toList();
    }

    public static List<Long> extractProductIds(List<Map.Entry<Long, Long>> entries) {
        return entries.stream()
                .map(Map.Entry::getKey)
                .toList();
    }
}
