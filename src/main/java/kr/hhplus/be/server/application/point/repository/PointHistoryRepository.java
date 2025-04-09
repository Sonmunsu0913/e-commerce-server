package kr.hhplus.be.server.application.point.repository;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;

import java.util.List;

public interface PointHistoryRepository {

    void save(PointHistory history);

    List<PointHistory> findAllByUserId(long userId);
}
