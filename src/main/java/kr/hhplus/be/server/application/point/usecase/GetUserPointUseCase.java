package kr.hhplus.be.server.application.point.usecase;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.stereotype.Service;

@Service
public class GetUserPointUseCase {

    private final PointRepository pointRepository;

    public GetUserPointUseCase(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public UserPoint execute(long userId) {
        return pointRepository.findById(userId);
    }
}
