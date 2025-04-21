package kr.hhplus.be.server.domain.point;

public interface UserPointRepository {

    UserPoint findById(long id);

    void save(UserPoint point);

    UserPoint findWithPessimisticLockById(long userId); // 비관적 락 메서드
}
