package kr.hhplus.be.server.application.point.repository;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;

import java.util.List;

public interface PointHistoryRepository {
    void save(long userId, long amount, PointTransactionType type, long updateMillis);
    List<PointHistory> findAllByUserId(long userId);
}
