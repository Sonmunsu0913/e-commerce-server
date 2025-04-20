package kr.hhplus.be.server.domain.point;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserPointTest {

@Test
void 포인트_정상_충전() {
    UserPoint userPoint = UserPoint.empty(1L);
    LocalDateTime before = userPoint.updatedAt();

    UserPoint charged = userPoint.charge(5000L);

    assertEquals(5000L, charged.point());
    assertTrue(charged.updatedAt().isAfter(before) || charged.updatedAt().isEqual(before));
}

@Test
void 포인트_과도한_충전_예외() {
    UserPoint userPoint = new UserPoint(1L, 999_000L, now(), now());

    assertThrows(IllegalStateException.class, () -> userPoint.charge(2000L));
}

@Test
void 포인트_정상_사용() {
    UserPoint userPoint = new UserPoint(1L, 5000L, now(), now());

    UserPoint used = userPoint.use(3000L);

    assertEquals(2000L, used.point());
}

@Test
void 포인트_부족_사용_예외() {
    UserPoint userPoint = new UserPoint(1L, 1000L, now(), now());

    assertThrows(IllegalStateException.class, () -> userPoint.use(2000L));
}

private LocalDateTime now() {
    return LocalDateTime.now();
}
}
