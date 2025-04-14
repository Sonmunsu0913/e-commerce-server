package kr.hhplus.be.server.application.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.application.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.application.point.usecase.ChargePointUseCase;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChargePointUseCaseTest {

    @Mock
    PointRepository pointRepository;

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    ChargePointUseCase useCase;

    @Test
    void 포인트_충전_정상() {
        // given: 사용자 ID와 초기 포인트 상태, 충전할 금액을 설정하고 Mock 리턴값을 정의
        long userId = 1L;
        long amount = 5000L;
        UserPoint current = UserPoint.empty(userId);

        when(pointRepository.findById(userId)).thenReturn(current);
        doNothing().when(pointRepository).save(any());
        doNothing().when(pointHistoryRepository).save(any());

        // when: 충전 유즈케이스 실행
        UserPoint result = useCase.execute(userId, amount);

        // then: 충전 결과와 포인트 이력 저장이 정상적으로 수행됐는지 검증
        assertEquals(5000L, result.point());
        verify(pointRepository).save(any());
        verify(pointHistoryRepository).save(any());
    }

}

