package kr.hhplus.be.server.application.product.repository;

import java.util.*;
import kr.hhplus.be.server.domain.product.Product;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
}
