package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.GetCouponService;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.order.service.GetOrderService;
import kr.hhplus.be.server.domain.order.service.ValidatePaymentService;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.service.RecordProductSaleService;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class OrderFacade {

    private final CreateOrderService createOrderService;
    private final GetOrderService getOrderService;
    private final ValidatePaymentService validatePaymentService;
    private final UsePointService usePointService;
    private final GetUserPointService getUserPointService;
    private final RecordProductSaleService recordProductSaleService;
    private final MockOrderReporter reporter;
    private final GetUserCouponService getUserCouponService;
    private final GetCouponService getCouponService;

    public OrderFacade(CreateOrderService createOrderService,
                       GetOrderService getOrderService,
                       ValidatePaymentService validatePaymentService,
                       UsePointService usePointService,
                       GetUserPointService getUserPointService,
                       RecordProductSaleService recordProductSaleService,
                       MockOrderReporter reporter, GetUserCouponService getUserCouponService,
                       GetCouponService getCouponService) {
        this.createOrderService = createOrderService;
        this.getOrderService = getOrderService;
        this.validatePaymentService = validatePaymentService;
        this.usePointService = usePointService;
        this.getUserPointService = getUserPointService;
        this.recordProductSaleService = recordProductSaleService;
        this.reporter = reporter;
        this.getUserCouponService = getUserCouponService;
        this.getCouponService = getCouponService;
    }

    public OrderResult order(CreateOrderCommand command) {

        // 0. 쿠폰 처리 준비
        int discountAmount = 0;
        UserCoupon userCoupon = null;
        if (command.couponId() != null) {
            // 0-1. 사용자 보유 쿠폰 조회
            userCoupon = getUserCouponService.execute(command.userId(), command.couponId());

            // 0-2. 쿠폰 사용 여부 체크
            if (userCoupon.isUsed()) {
                throw new IllegalStateException("이미 사용한 쿠폰입니다.");
            }

            // 0-3. 쿠폰 상세 조회
            Coupon coupon = getCouponService.execute(command.couponId());

            // 0-4. 할인 금액 추출
            discountAmount = coupon.getDiscountAmount();
        }

        // 1. 주문 생성
        Order order = createOrderService.execute(
            command.userId(),
            command.items(),
            command.couponId()
        );

        // 2. 사용자 현재 포인트 조회
        UserPoint current = getUserPointService.execute(order.getUserId());

        // 3. 결제 가능 여부 검증 (포인트 부족 시 예외 발생)
        validatePaymentService.execute(order, (int) current.point());

        // 4. 포인트 차감
        UserPoint updated = usePointService.execute(order.getUserId(), order.getFinalPrice());

        // 5. 판매 기록 저장
        LocalDate today = LocalDate.now();
        for (OrderItemCommand item : command.items()) {
            recordProductSaleService.execute(new ProductSale(item.productId(), today, item.quantity()));
        }

        // 6. 응답 생성
        OrderResult result = new OrderResult(
                order.getId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                order.getOrderedAt(),
                (int) updated.point()
        );

        reporter.send(order.toResponse((int) updated.point()));

        return result;
    }

    public PaymentResult pay(Long id) {
        // 1. 주문 ID로 주문 조회 (존재하지 않으면 예외 발생)
        Order order = getOrderService.execute(id);

        // 2. 사용자 현재 포인트 조회
        UserPoint current = getUserPointService.execute(order.getUserId());

        // 3. 포인트가 결제 금액보다 충분한지 검증
        validatePaymentService.execute(order, (int) current.point());

        // 4. 포인트 차감 (결제)
        UserPoint updated = usePointService.execute(order.getUserId(), order.getFinalPrice());

        // 5. 각 상품별 판매 기록 저장
        for (OrderItemCommand item : order.getItems()) {
            recordProductSaleService.execute(
                    new ProductSale(item.productId(), LocalDate.now(), item.quantity())
            );
        }

        // 6. 결제 결과 응답 생성 후 반환
        OrderResult orderResult = new OrderResult(
            order.getId(),
            order.getTotalPrice(),
            order.getDiscount(),
            order.getFinalPrice(),
            order.getOrderedAt(),
            (int) updated.point()
        );
        return new PaymentResult(orderResult, (int) updated.point());
    }
}

