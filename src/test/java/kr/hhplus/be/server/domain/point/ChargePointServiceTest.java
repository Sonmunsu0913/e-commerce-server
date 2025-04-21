package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.point.service.ChargePointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChargePointServiceTest {

    @Mock
    UserPointRepository userPointRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    ChargePointService useCase;

    @Test
    void 포인트_충전_정상() {
        // given
        long userId = 1L;
        long amount = 5000L;
        UserPoint current = new UserPoint(userId, 0L, LocalDateTime.now(), LocalDateTime.now());

        when(userPointRepository.findWithPessimisticLockById(userId)).thenReturn(current);
        doNothing().when(userPointRepository).save(any());
        doNothing().when(pointHistoryRepository).save(any());

        // when
        UserPoint result = useCase.execute(userId, amount);

        // then
        assertEquals(5000L, result.point());
        verify(userPointRepository).save(any());
        verify(pointHistoryRepository).save(any());
    }
}
