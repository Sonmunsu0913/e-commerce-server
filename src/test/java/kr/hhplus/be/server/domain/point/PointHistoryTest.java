package kr.hhplus.be.server.domain.point;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    void 포인트_히스토리_생성_정상() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        PointHistory history = new PointHistory(
            null,
            1L,
            1000L,
            PointTransactionType.CHARGE,
            now,
            now
        );

        // then
        assertNull(history.id());
        assertEquals(1L, history.userId());
        assertEquals(1000L, history.amount());
        assertEquals(PointTransactionType.CHARGE, history.type());
        assertEquals(now, history.createdAt());
        assertEquals(now, history.updatedAt());
    }

    @Test
    void 포인트_히스토리_타입_USE_정상() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        PointHistory history = new PointHistory(
            1L,
            2L,
            300L,
            PointTransactionType.USE,
            now,
            now
        );

        // then
        assertEquals(PointTransactionType.USE, history.type());
        assertEquals(now, history.createdAt());
        assertEquals(now, history.updatedAt());
    }
}
