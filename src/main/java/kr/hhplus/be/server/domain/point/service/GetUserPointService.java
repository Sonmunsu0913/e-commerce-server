package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.UserPointRepository;
import kr.hhplus.be.server.domain.point.UserPoint;
import org.springframework.stereotype.Service;

@Service
public class GetUserPointService {

    private final UserPointRepository userPointRepository;

    public GetUserPointService(UserPointRepository userPointRepository) {
        this.userPointRepository = userPointRepository;
    }

    public UserPoint execute(long userId) {
        return userPointRepository.findById(userId);
    }
}
