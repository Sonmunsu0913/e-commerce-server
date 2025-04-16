package kr.hhplus.be.server.application.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;
import kr.hhplus.be.server.domain.point.service.GetPointHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPointHistoryServiceTest {

    @Mock
    PointHistoryRepository pointHistoryRepository;

    @InjectMocks
    GetPointHistoryService useCase;

    @Test
    void 포인트_이력_정상_조회() {
        // given
        long userId = 1L;
        List<PointHistory> histories = List.of(
            new PointHistory(1L, userId, 1000L, PointTransactionType.CHARGE, LocalDateTime.now(), LocalDateTime.now()),
            new PointHistory(2L, userId, 500L, PointTransactionType.USE, LocalDateTime.now(), LocalDateTime.now())
        );

        when(pointHistoryRepository.findAllByUserId(userId)).thenReturn(histories);

        // when
        List<PointHistory> result = useCase.execute(userId);

        // then
        assertEquals(2, result.size());
        assertEquals(PointTransactionType.CHARGE, result.get(0).type());
        assertEquals(PointTransactionType.USE, result.get(1).type());
    }
}
