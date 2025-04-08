package kr.hhplus.be.server.application.point.repository;

import kr.hhplus.be.server.domain.point.UserPoint;

public interface PointRepository {
    UserPoint findById(long id);
    void save(UserPoint point);
}
