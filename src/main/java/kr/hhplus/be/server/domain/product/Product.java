package kr.hhplus.be.server.domain.product;

/**
 * 상품 정보를 나타내는 도메인 모델 클래스.
 * 상품 ID, 이름, 가격, 재고 수량을 보유하며 재고 확인 및 감소 등의 비즈니스 로직을 제공한다.
 */
public record Product(
    Long id,    // 상품 고유 ID (식별자)
    String name,    // 상품명
    int price,    // 상품 가격
    int stock    // 현재 재고 수량
) {
    /**
     * 주어진 수량만큼의 재고가 있는지 확인한다.
     * @param quantity 확인할 상품 수량
     * @return 현재 재고(stock)가 quantity 이상이면 true, 그렇지 않으면 false
     */
    public boolean isAvailable(int quantity) {
        return stock >= quantity;
    }

    /**
     * 재고를 주어진 수량만큼 감소시킨 새로운 Product 객체를 반환한다.
     * <p>현재 Product는 불변(immutable)이므로, 재고를 변경한 새 Product 인스턴스를 생성하여 반환한다.</p>
     * @param quantity 감소시킬 상품 수량
     * @return stock에서 quantity를 차감한 새로운 Product (변경된 재고 반영)
     * @throws IllegalArgumentException 재고(stock)가 요청 수량보다 부족한 경우 발생
     */
    public Product reduceStock(int quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("재고가 부족합니다.");  // 재고 부족 시 예외 처리
        }
        return new Product(id, name, price, stock - quantity);        // 감소된 재고로 새로운 Product 생성
    }
}
