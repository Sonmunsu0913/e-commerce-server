package kr.hhplus.be.server.domain.product;

import java.util.*;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findAllByIdIn(List<Long> ids);

    void save(Product product);

    Product findWithPessimisticLockById(Long id);   // 비관적 락 메서드
}
