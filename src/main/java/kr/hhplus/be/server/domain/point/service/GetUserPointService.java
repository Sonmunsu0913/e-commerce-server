package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.stereotype.Service;

@Service
public class GetUserPointService {

    private final PointRepository pointRepository;

    public GetUserPointService(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    public UserPoint execute(long userId) {
        return pointRepository.findById(userId);
    }
}
