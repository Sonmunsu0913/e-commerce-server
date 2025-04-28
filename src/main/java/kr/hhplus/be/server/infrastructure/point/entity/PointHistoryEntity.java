package kr.hhplus.be.server.infrastructure.point.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointTransactionType;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
    name = "point_history",
    indexes = {
        @Index(name = "idx_user_id", columnList = "userId")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public PointHistoryEntity(long userId, long amount, PointTransactionType type, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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
