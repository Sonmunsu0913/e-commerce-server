package kr.hhplus.be.server.domain.product;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 여러 상품 판매 기록(ProductSale 리스트)을 받아 상품별 판매 통계를 계산하는 도메인 도우미 클래스.
 * <p>상품별 총 판매 수량을 집계하고, 가장 인기 있는 상품 순위(Top N) 등을 산출하는 기능을 제공한다.</p>
 */
public class ProductSaleStatistics {

    private final Map<Long, Long> totalSalesPerProduct; // 상품별 총 판매 수량 맵 (key: 상품 ID, value: 해당 상품의 판매 수량 합계)

    /**
     * 생성자 - 외부에서 호출되지 않고, 정적 팩토리 메서드(of)를 통해 생성한다.
     * @param totalSalesPerProduct 상품별 총 판매량을 나타내는 맵
     */
    private ProductSaleStatistics(Map<Long, Long> totalSalesPerProduct) {
        this.totalSalesPerProduct = totalSalesPerProduct;
    }

    /**
     * 주어진 상품 판매 기록 목록을 기반으로 상품별 총 판매량을 집계하여 ProductSaleStatistics 인스턴스를 생성한다.
     * <p>리스트의 각 ProductSale에서 상품 ID별로 판매 수량을 합산(groupingBy 및 summingLong 사용)한다.</p>
     * @param sales 상품 판매 기록 리스트
     * @return 집계된 상품별 판매량 정보를 담은 ProductSaleStatistics 객체
     */
    public static ProductSaleStatistics of(List<ProductSale> sales) {
        // 스트림을 사용하여 상품 ID별로 판매 수량 합계를 계산한다.
        Map<Long, Long> map = sales.stream()
            .collect(Collectors.groupingBy(
                ProductSale::productId,
                Collectors.summingLong(ProductSale::quantity)
            ));
        return new ProductSaleStatistics(map);
    }

    /**
     * 총 판매량 상위 N개의 상품 엔트리 목록을 반환한다.
     * <p>내부의 상품별 판매량 맵(totalSalesPerProduct)을 판매 수량 기준으로 내림차순 정렬하여 상위 limit개의 (상품ID, 총판매량) 엔트리를 추출한다.</p>
     * @param limit 상위 몇 개의 상품을 가져올지 지정하는 값 (N 값)
     * @return 판매량이 높은 순으로 정렬된 상위 N개의 상품 ID와 총 판매량 쌍의 리스트 (Map.Entry 형태)
     */
    public List<Map.Entry<Long, Long>> topN(int limit) {
        return totalSalesPerProduct.entrySet().stream()
            .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())  // 판매 수량(value) 내림차순 정렬
            .limit(limit)                                                // 상위 N개 선택
            .toList();                                                   // 결과를 리스트로 변환
    }

    /**
     * 주어진 판매량 엔트리 목록에서 상품 ID들만 추출하여 리스트로 반환한다.
     * <p>예를 들어, topN 메서드의 결과에서 상위 상품들의 ID 목록만 필요할 때 사용한다.</p>
     * @param entries 상품 ID와 총 판매량으로 이루어진 엔트리 리스트
     * @return entries 리스트에 포함된 상품 ID만을 모은 리스트
     */
    public List<Long> extractProductIds(List<Map.Entry<Long, Long>> entries) {
        return entries.stream()
            .map(Map.Entry::getKey)  // 각 엔트리의 상품 ID 추출
            .toList();
    }

    /**
     * 상품별 총 판매량 맵을 반환한다.
     * @return 내부에 저장된 <상품ID, 총판매량> 맵 (수정 불가한 불변 객체)
     */
    public Map<Long, Long> getTotalSalesPerProduct() {
        return totalSalesPerProduct;
    }
}


