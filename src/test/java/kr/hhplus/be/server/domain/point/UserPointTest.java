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
    UserPoint userPoint = new UserPoint(1L, 999_000L, now(), now(), 0);

    assertThrows(IllegalStateException.class, () -> userPoint.charge(2000L));
}

@Test
void 포인트_정상_사용() {
    UserPoint userPoint = new UserPoint(1L, 5000L, now(), now(), 0);

    UserPoint used = userPoint.use(3000L);

    assertEquals(2000L, used.point());
}

@Test
void 포인트가_부족한_경우_포인트_차감시_IllegalStateException을_발생시킨다() {
    UserPoint userPoint = new UserPoint(1L, 1000L, now(), now(), 0);

    assertThrows(IllegalStateException.class, () -> userPoint.use(2000L));
}

private LocalDateTime now() {
    return LocalDateTime.now();
}
}
