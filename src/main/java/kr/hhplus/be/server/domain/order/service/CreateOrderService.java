package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItemCommand;

import java.util.ArrayList;
import java.util.List;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateOrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final CouponRepository couponRepository;

    public CreateOrderService(OrderRepository orderRepository, ProductRepository productRepository, CouponRepository couponRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.couponRepository = couponRepository;
    }

    public Order execute(Long userId, List<OrderItemCommand> items, Long couponId) {
        int discount = applyCouponPolicy(couponId);

        // 1. 각 상품에 대해 낙관적 락으로 조회 + 재고 차감
        List<OrderItemCommand> updatedItems = new ArrayList<>();

        for (OrderItemCommand item : items) {
            // 1-1. 락 걸고 상품 조회
            Product product = productRepository.findWithPessimisticLockById(item.productId());

//            Product product = productRepository.findWithOptimisticLockById(item.productId());

            // 1-2. 재고 차감
            product.reduceStock(item.quantity());

//            if (updatedProduct.stock() < 0) {
//                throw new IllegalStateException("재고가 부족합니다.");
//            }

            // 1-3. 저장
            productRepository.save(product);

//            try {
//                productRepository.save(updatedProduct);
//            } catch (ObjectOptimisticLockingFailureException e) {
//                throw new IllegalStateException("상품 '" + product.name() + "'의 재고가 부족하거나 다른 주문이 먼저 처리되었습니다. 다시 시도해주세요.");
//            }

            // 1-4. 실제 반영된 가격으로 주문 항목 생성
            updatedItems.add(new OrderItemCommand(
                    product.id(),
                    product.name(),
                    product.price(),
                    item.quantity()
            ));
        }

        // 2. 최종 Order 객체 생성 및 저장
        Order order = new Order(null, userId, updatedItems, discount);
        return orderRepository.save(order);
    }

    private int applyCouponPolicy(Long couponId) {
        if (couponId == null) return 0;

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다."));
        return coupon.getDiscountAmount();
    }
}
