package kr.hhplus.be.server.domain.order;

/**
 * 주문 결제 도메인 객체
 * - 주문(Order)을 기반으로 결제 검증 로직을 수행
 */
public class Payment {

    private final Order order;

    /**
     * 결제 대상 주문을 생성자에서 받아 구성
     * @param order 결제를 수행할 주문 객체
     */
    public Payment(Order order) {
        this.order = order;
    }

    /**
     * 사용자의 현재 포인트가 결제 금액보다 충분한지 검증
     * - 부족할 경우 예외 발생
     *
     * @param currentPoint 사용자의 현재 포인트 잔액
     * @throws IllegalStateException 포인트가 부족한 경우
     */
    public void validateEnoughPoint(int currentPoint) {
        if (currentPoint < order.getFinalPrice()) {
            throw new IllegalStateException("포인트 부족");
        }
    }
}

