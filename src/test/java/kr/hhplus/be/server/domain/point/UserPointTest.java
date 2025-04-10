package kr.hhplus.be.server.domain.point;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

    @Test
    void 포인트_정상_충전() {
        UserPoint userPoint = UserPoint.empty(1L);

        UserPoint charged = userPoint.charge(5000L);

        assertEquals(5000L, charged.point());
        System.out.println("before: " + userPoint.updateMillis());
        System.out.println("after: " + charged.updateMillis());
        assertTrue(charged.updateMillis() >= userPoint.updateMillis());
    }

    @Test
    void 포인트_과도한_충전_예외() {
        UserPoint userPoint = new UserPoint(1L, 999_000L, System.currentTimeMillis());

        assertThrows(IllegalStateException.class, () -> {
            userPoint.charge(2000L); // 최대 100만 초과
        });
    }

    @Test
    void 포인트_정상_사용() {
        UserPoint userPoint = new UserPoint(1L, 5000L, System.currentTimeMillis());

        UserPoint used = userPoint.use(3000L);

        assertEquals(2000L, used.point());
    }

    @Test
    void 포인트_부족_사용_예외() {
        UserPoint userPoint = new UserPoint(1L, 1000L, System.currentTimeMillis());

        assertThrows(IllegalStateException.class, () -> {
            userPoint.use(2000L); // 부족
        });
    }
}