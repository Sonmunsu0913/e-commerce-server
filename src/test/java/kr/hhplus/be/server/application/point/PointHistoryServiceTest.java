package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.point.service.PointHistoryService;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PointHistoryServiceTest {

    private final PointHistoryRepository pointHistoryRepository = Mockito.mock(
        PointHistoryRepository.class);
    private final PointHistoryService pointHistoryService = new PointHistoryService(
        pointHistoryRepository);

    @Test
    void 포인트_거래_기록_조회_정상작동() {
        // given
        long userId = 1L;
        PointHistory mockHistory1 = new PointHistory(
            1L, userId, 1000L, PointTransactionType.CHARGE, LocalDateTime.now()
        );
        PointHistory mockHistory2 = new PointHistory(
            2L, userId, 500L, PointTransactionType.USE, LocalDateTime.now()
        );

        // when
        when(pointHistoryRepository.findAllByUserId(userId)).thenReturn(
            List.of(mockHistory1, mockHistory2));

        // when
        List<PointHistory> historyList = pointHistoryService.getHistory(userId);

        // then
        assertNotNull(historyList);
        assertEquals(2, historyList.size());  // 2개의 기록이 반환되어야 함
        assertEquals(mockHistory1, historyList.get(0));  // 첫 번째 기록 검증
        assertEquals(mockHistory2, historyList.get(1));  // 두 번째 기록 검증
    }

    @Test
    void 존재하지_않는_유저_포인트_기록_조회_빈_리스트() {
        // given
        long userId = 2L;

        // when
        when(pointHistoryRepository.findAllByUserId(userId)).thenReturn(List.of());

        // when
        List<PointHistory> historyList = pointHistoryService.getHistory(userId);

        // then
        assertNotNull(historyList);
        assertTrue(historyList.isEmpty());  // 기록이 없으면 빈 리스트가 반환되어야 함
    }
}