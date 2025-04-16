package kr.hhplus.be.server.application.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.usecase.GetUserPointUseCase;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetUserPointUseCaseTest {

    @Mock
    PointRepository pointRepository;

    @InjectMocks
    GetUserPointUseCase useCase;

    @Test
    void 포인트_조회_정상() {
        // given
        long userId = 1L;
        UserPoint userPoint = new UserPoint(userId, 1000L, LocalDateTime.now(), LocalDateTime.now());

        when(pointRepository.findById(userId)).thenReturn(userPoint);

        // when
        UserPoint result = useCase.execute(userId);

        // then
        assertEquals(1000L, result.point());
    }
}
