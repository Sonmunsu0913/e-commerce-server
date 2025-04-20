package kr.hhplus.be.server.infrastructure.order.repository;

import kr.hhplus.be.server.infrastructure.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
}
