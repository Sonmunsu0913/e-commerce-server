package kr.hhplus.be.server.domain.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserPointServiceTest {

    @Mock
    UserPointRepository userPointRepository;

    @InjectMocks
    GetUserPointService useCase;

    @Test
    void 포인트_조회_정상() {
        // given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 1000L, LocalDateTime.now(), LocalDateTime.now(), 0);

        when(userPointRepository.findById(userId)).thenReturn(userPoint);

        // when
        UserPoint result = useCase.execute(userId);

        // then
        assertEquals(1000L, result.point());
    }
}
