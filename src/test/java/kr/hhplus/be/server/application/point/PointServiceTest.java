package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PointServiceTest {

    private final PointRepository pointRepository = Mockito.mock(PointRepository.class);
    private final PointHistoryRepository pointHistoryRepository = Mockito.mock(PointHistoryRepository.class);
    private final PointService pointService = new PointService(pointRepository, pointHistoryRepository);

    @Test
    void 포인트_충전_정상작동() {
        // given
        Long userId = 1L;
        long amount = 1000L;
        UserPoint mockUserPoint = new UserPoint(userId, 5000L, System.currentTimeMillis());

        when(pointRepository.findById(userId)).thenReturn(mockUserPoint);

        // when
        UserPoint updatedUserPoint = pointService.charge(userId, amount);

        // then
        assertNotNull(updatedUserPoint);
        assertEquals(6000L, updatedUserPoint.point());  // 5000 + 1000 = 6000
        verify(pointRepository).save(updatedUserPoint);
        verify(pointHistoryRepository).save(any(PointHistory.class));  // PointHistory가 저장되는지 확인
    }

    @Test
    void 포인트_사용_정상작동() {
        // given
        Long userId = 1L;
        long amount = 1000L;
        UserPoint mockUserPoint = new UserPoint(userId, 5000L, System.currentTimeMillis());

        when(pointRepository.findById(userId)).thenReturn(mockUserPoint);

        // when
        UserPoint updatedUserPoint = pointService.use(userId, amount);

        // then
        assertNotNull(updatedUserPoint);
        assertEquals(4000L, updatedUserPoint.point());  // 5000 - 1000 = 4000
        verify(pointRepository).save(updatedUserPoint);
        verify(pointHistoryRepository).save(any(PointHistory.class));  // PointHistory가 저장되는지 확인
    }

    @Test
    void 포인트_조회_정상작동() {
        // given
        Long userId = 1L;
        UserPoint mockUserPoint = new UserPoint(userId, 5000L, System.currentTimeMillis());

        when(pointRepository.findById(userId)).thenReturn(mockUserPoint);

        // when
        UserPoint userPoint = pointService.getPoint(userId);

        // then
        assertNotNull(userPoint);
        assertEquals(userId, userPoint.id());
        assertEquals(5000L, userPoint.point());  // mockUserPoint에 설정된 값
    }

    @Test
    void 포인트_사용_포인트_부족_예외() {
        // given
        Long userId = 1L;
        long amount = 6000L;  // 요청한 사용 금액이 보유 포인트보다 많음
        UserPoint mockUserPoint = new UserPoint(userId, 5000L, System.currentTimeMillis());

        when(pointRepository.findById(userId)).thenReturn(mockUserPoint);

        // when & then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pointService.use(userId, amount);  // 포인트 부족으로 사용 불가
        });

        assertEquals("포인트 부족", exception.getMessage());
    }
}
