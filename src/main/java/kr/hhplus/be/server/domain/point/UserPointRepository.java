package kr.hhplus.be.server.domain.point;

public interface UserPointRepository {

    UserPoint findById(long id);

    void save(UserPoint point);

}
