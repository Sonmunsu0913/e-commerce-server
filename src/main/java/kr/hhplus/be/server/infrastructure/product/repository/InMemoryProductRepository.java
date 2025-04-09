package kr.hhplus.be.server.infrastructure.product.repository;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> store = new HashMap<>();

    public InMemoryProductRepository() {
        store.put(1L, new Product(1L, "콜라", 1500, 10));
        store.put(2L, new Product(2L, "사이다", 1400, 5));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
