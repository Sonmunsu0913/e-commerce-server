package kr.hhplus.be.server.domain.product;

import java.util.*;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    List<Product> findAllByIdIn(List<Long> ids);

    Product save(Product product);

    Product findWithPessimisticLockById(Long id);   // 비관적 락 메서드

    Product findWithOptimisticLockById(Long id);   // 낙관적 락 메서드
}
