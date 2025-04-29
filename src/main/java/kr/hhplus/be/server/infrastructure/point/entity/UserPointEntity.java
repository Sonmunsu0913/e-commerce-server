package kr.hhplus.be.server.infrastructure.point.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import kr.hhplus.be.server.domain.point.UserPoint;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(
    name = "user_point",
    indexes = {
        @Index(name = "idx_id", columnList = "id")
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPointEntity {

    @Id
    private Long id; // 사용자 ID가 PK

    private long point;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Version
    private int version;  // 낙관적 락용 필드

    public UserPointEntity(Long id, long point, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.point = point;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserPointEntity from(UserPoint domain) {
        UserPointEntity entity = new UserPointEntity(
            domain.id(),
            domain.point(),
            domain.createdAt(),
            domain.updatedAt()
        );
        entity.version = domain.version(); // version 수동 세팅
        return entity;
    }

    public UserPoint toDomain() {
        return new UserPoint(id, point, createdAt, updatedAt, version);
    }
}
