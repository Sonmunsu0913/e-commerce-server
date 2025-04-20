package kr.hhplus.be.server.infrastructure.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;

import java.time.LocalDateTime;

@Entity
@Table(name = "point_history")
public class PointHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;

    private long amount;

    @Enumerated(EnumType.STRING)
    private PointTransactionType type;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    protected PointHistoryEntity() {}

    public PointHistoryEntity(long userId, long amount, PointTransactionType type, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public static PointHistoryEntity from(PointHistory domain) {
        return new PointHistoryEntity(
                domain.userId(),
                domain.amount(),
                domain.type(),
                domain.createdAt(),
                domain.updatedAt()
        );
    }

    public PointHistory toDomain(Long id) {
        return new PointHistory(id, userId, amount, type, createdAt, updatedAt);
    }
}

