package kr.hhplus.be.server.domain.order;

import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.*;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import lombok.Getter;

/**
 * 주문 도메인 모델
 * - 사용자와 상품 정보, 가격, 할인, 결제 금액, 주문 시간 등을 포함한다.
 * - 주문에 대한 응답 객체로 변환할 수 있는 기능도 제공한다.
 */
@Getter
@Table(name = "`order`")
public class Order {

    private final Long id;                          // 주문 고유 ID
    private final Long userId;                      // 주문한 사용자 ID
    private final List<OrderItemCommand> items;     // 주문한 상품 목록
    private final int totalPrice;                   // 전체 주문 금액 (할인 전)
    private final int discount;                     // 적용된 할인 금액
    private final int finalPrice;                   // 실제 결제 금액 (할인 적용 후)
    private final String orderedAt;                 // 주문 시각 (ISO-8601 문자열)

    /**
     * 주문 생성자
     * - 주문 ID, 사용자 ID, 상품 목록, 할인 금액을 받아 주문 정보를 구성한다.
     * - totalPrice, finalPrice, orderedAt 값을 자동 계산/생성한다.
     *
     * @param id 주문 ID
     * @param userId 사용자 ID
     * @param items 주문한 상품 목록
     * @param discount 할인 금액
     */
    public Order(Long id, Long userId, List<OrderItemCommand> items, int discount) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.totalPrice = items.stream().mapToInt(OrderItemCommand::subtotal).sum();  // 주문 상품들의 총 금액 계산
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
            id,
            totalPrice,
            discount,
            finalPrice,
            pointAfterPayment,
            orderedAt
        );
    }

    /**
     * 기존 주문 객체에서 주문 ID만 새로 지정하여 새로운 주문 객체를 생성합니다.
     * - 주로 주문 ID가 생성 시점에 결정되는 경우 (예: DB에서 시퀀스로 할당) 사용됩니다.
     *
     * @param newId 새로 할당할 주문 ID
     * @return 주문 ID가 적용된 새 Order 객체
     */
    public Order withOrderId(Long newId) {
        return new Order(
                newId,
                this.userId,
                this.items,
                this.discount
        );
    }
}


