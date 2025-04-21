package kr.hhplus.be.server.domain.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsePointServiceTest {

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    UsePointService useCase;

    @Test
    void 포인트_정상_사용() {
        // given
        long userId = 1L;
        long amount = 3000L;
        UserPoint current = new UserPoint(userId, 5000L, LocalDateTime.now(), LocalDateTime.now());

        when(userPointRepository.findById(userId)).thenReturn(current);
        doNothing().when(userPointRepository).save(any());
        doNothing().when(pointHistoryRepository).save(any());

        // when
        UserPoint result = useCase.execute(userId, amount);

        // then
        assertEquals(2000L, result.point());
        verify(userPointRepository).save(any());
        verify(pointHistoryRepository).save(any());
    }

    @Test
    void 포인트_부족_사용_예외() {
        // given
        long userId = 1L;
        long amount = 10_000L;
        UserPoint current = new UserPoint(userId, 3000L, LocalDateTime.now(), LocalDateTime.now());

        when(userPointRepository.findById(userId)).thenReturn(current);

        // when & then
        assertThrows(IllegalStateException.class, () -> useCase.execute(userId, amount));
    }
}
