package kr.hhplus.be.server.domain.product;

import java.time.LocalDate;

/**
 * 상품 판매 이력을 표현하는 도메인 모델 클래스.
 * 특정 상품이 언제 몇 개 판매되었는지를 기록하는 불변(immutable) 데이터 객체이다.
 */
public record ProductSale(
    Long productId,      // 판매된 상품의 ID
    LocalDate saleDate,  // 판매 날짜
    int quantity         // 판매 수량
) {
}
