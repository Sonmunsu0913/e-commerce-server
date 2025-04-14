package kr.hhplus.be.server.domain.point;

import java.time.LocalDateTime;

/**
 * 사용자 포인트 도메인 모델
 * - 포인트 충전, 사용, 상태 변경을 불변 객체로 관리함
 */
public record UserPoint(
    Long id,                        // 사용자 ID
    long point,                     // 현재 포인트 잔액
    LocalDateTime createdAt,        // 생성 시간
    LocalDateTime updatedAt         // 업데이트 시간
) {

    /**
     * 포인트가 없는 초기 상태를 생성하는 정적 팩토리 메서드
     */
    public static UserPoint empty(long id) {
        LocalDateTime now = LocalDateTime.now();
        return new UserPoint(id, 0, now, now);
    }
    /**
     * 포인트를 충전함
     * - 최대 잔액은 1,000,000 포인트
     * @param amount 충전할 포인트
     * @return 잔액이 증가된 새로운 UserPoint 객체
     */
    public UserPoint charge(long amount) {
        if (this.point + amount > 1_000_000L) {
            throw new IllegalStateException("최대 잔고를 넘을 수 없습니다. (최대: 1,000,000)");
        }
        return new UserPoint(this.id, this.point + amount, this.createdAt, LocalDateTime.now());
    }

    /**
     * 포인트를 사용함
     * - 현재 잔액보다 많은 포인트를 사용할 경우 예외 발생
     * @param amount 사용할 포인트
     * @return 잔액이 감소된 새로운 UserPoint 객체
     */
    public UserPoint use(long amount) {
        if (this.point < amount) {
            throw new IllegalStateException("포인트 부족");
        }
        return new UserPoint(this.id, this.point - amount, this.createdAt, LocalDateTime.now());
    }

}
