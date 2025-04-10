package kr.hhplus.be.server.domain.order;

public class Payment {

    private final Order order;

    public Payment(Order order) {
        this.order = order;
    }

    public void validateEnoughPoint(int currentPoint) {
        if (currentPoint < order.getFinalPrice()) {
            throw new IllegalStateException("포인트 부족");
        }
    }
}
