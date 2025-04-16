package kr.hhplus.be.server.infrastructure.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.UserPoint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_point",
        indexes = {
                @Index(name = "idx_user_id", columnList = "userId")
        }
)
public class UserPointEntity {

    @Id
    private Long id; // 사용자 ID가 PK

    private long point;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    protected UserPointEntity() {}

    public UserPointEntity(Long id, long point, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserPointEntity from(UserPoint domain) {
        return new UserPointEntity(domain.id(), domain.point(), domain.createdAt(), domain.updatedAt());
    }

    public UserPoint toDomain() {
        return new UserPoint(id, point, createdAt, updatedAt);
    }

    public Long getId() {
        return id;
    }
}

