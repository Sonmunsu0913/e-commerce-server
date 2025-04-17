package kr.hhplus.be.server.domain.point;

public interface PointRepository {

    UserPoint findById(long id);

    void save(UserPoint point);

}
