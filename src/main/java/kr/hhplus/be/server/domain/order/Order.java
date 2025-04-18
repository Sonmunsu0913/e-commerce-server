package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.*;
import kr.hhplus.be.server.domain.order.dto.OrderItemRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import lombok.Getter;

/**
 * 주문 도메인 모델
 * - 사용자와 상품 정보, 가격, 할인, 결제 금액, 주문 시간 등을 포함한다.
 * - 주문에 대한 응답 객체로 변환할 수 있는 기능도 제공한다.
 */
@Getter
public class Order {

    private final Long orderId;              // 주문 고유 ID
    private final Long userId;               // 주문한 사용자 ID
    private final List<OrderItemRequest> items;  // 주문한 상품 목록
    private final int totalPrice;            // 전체 주문 금액 (할인 전)
    private final int discount;              // 적용된 할인 금액
    private final int finalPrice;            // 실제 결제 금액 (할인 적용 후)
    private final String orderedAt;          // 주문 시각 (ISO-8601 문자열)

    /**
     * 주문 생성자
     * - 주문 ID, 사용자 ID, 상품 목록, 할인 금액을 받아 주문 정보를 구성한다.
     * - totalPrice, finalPrice, orderedAt 값을 자동 계산/생성한다.
     *
     * @param orderId 주문 ID
     * @param userId 사용자 ID
     * @param items 주문한 상품 목록
     * @param discount 할인 금액
     */
    public Order(Long orderId, Long userId, List<OrderItemRequest> items, int discount) {
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemRequest::subtotal).sum();  // 주문 상품들의 총 금액 계산
        this.discount = discount;
        this.finalPrice = totalPrice - discount;  // 할인 적용 후 결제 금액
        this.orderedAt = LocalDateTime.now().toString();  // 주문 시각 저장
    }

    /**
     * 주문 정보를 API 응답용 DTO로 변환
     *
     * @param pointAfterPayment 결제 후 남은 포인트 잔액
     * @return OrderResponse 응답 객체
     */
    public OrderResponse toResponse(int pointAfterPayment) {
        return new OrderResponse(
            orderId,
            totalPrice,
            discount,
            finalPrice,
            pointAfterPayment,
            orderedAt
        );
    }
}


