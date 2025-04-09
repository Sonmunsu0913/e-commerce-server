package kr.hhplus.be.server.application.order.repository;

import java.util.*;
import kr.hhplus.be.server.domain.order.Order;

public interface OrderRepository {

    Optional<Order> findById(Long orderId);

    void save(Order order);

}

