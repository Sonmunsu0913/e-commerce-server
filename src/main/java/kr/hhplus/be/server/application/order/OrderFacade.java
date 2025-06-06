package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.service.GetCouponService;
import kr.hhplus.be.server.domain.coupon.service.GetUserCouponService;
import kr.hhplus.be.server.domain.coupon.service.ValidateCouponService;
import kr.hhplus.be.server.domain.order.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.event.OrderRequestedEventData;
import kr.hhplus.be.server.domain.order.service.CreateOrderService;
import kr.hhplus.be.server.domain.order.service.GetOrderService;
import kr.hhplus.be.server.domain.order.service.ValidatePaymentService;
import kr.hhplus.be.server.domain.point.service.GetUserPointService;
import kr.hhplus.be.server.domain.point.service.UsePointService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import kr.hhplus.be.server.domain.point.UserPoint;
import kr.hhplus.be.server.domain.product.ProductSale;
import kr.hhplus.be.server.domain.product.event.ProductSaleEventPublisher;
import kr.hhplus.be.server.domain.product.service.RecordProductSaleService;
import kr.hhplus.be.server.domain.product.service.UpdateProductRankingService;
import kr.hhplus.be.server.domain.report.event.ReportEventPublisher;
import kr.hhplus.be.server.infrastructure.mock.MockOrderReporter;
import kr.hhplus.be.server.infrastructure.report.kafka.OrderReportEventPublisher;
import kr.hhplus.be.server.infrastructure.report.kafka.OrderReportMessage;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;

@Service
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
    private final UpdateProductRankingService updateProductRankingService;
    private final ValidateCouponService validateCouponService;
    private final OrderEventPublisher orderEventPublisher;
    private final ReportEventPublisher reportEventPublisher;
    private final ProductSaleEventPublisher productSaleEventPublisher;
    private final OrderReportEventPublisher orderReportEventPublisher;

    public OrderFacade(CreateOrderService createOrderService,
                        GetOrderService getOrderService,
                        ValidatePaymentService validatePaymentService,
                        UsePointService usePointService,
                        GetUserPointService getUserPointService,
                        RecordProductSaleService recordProductSaleService,
                        MockOrderReporter reporter, GetUserCouponService getUserCouponService,
                        GetCouponService getCouponService,
                        UpdateProductRankingService updateProductRankingService,
                        ValidateCouponService validateCouponService,
                        OrderEventPublisher orderEventPublisher,
                        ReportEventPublisher reportEventPublisher,
                        ProductSaleEventPublisher productSaleEventPublisher,
                        OrderReportEventPublisher orderReportEventPublisher) {
        this.createOrderService = createOrderService;
        this.getOrderService = getOrderService;
        this.validatePaymentService = validatePaymentService;
        this.usePointService = usePointService;
        this.getUserPointService = getUserPointService;
        this.recordProductSaleService = recordProductSaleService;
        this.reporter = reporter;
        this.getUserCouponService = getUserCouponService;
        this.getCouponService = getCouponService;
        this.updateProductRankingService = updateProductRankingService;
        this.validateCouponService = validateCouponService;
        this.orderEventPublisher = orderEventPublisher;
        this.reportEventPublisher = reportEventPublisher;
        this.productSaleEventPublisher = productSaleEventPublisher;
        this.orderReportEventPublisher = orderReportEventPublisher;
    }

    @Transactional
    public void orderV2(CreateOrderCommand command) {
        // 0. 쿠폰 처리 (사용자 보유 쿠폰 조회 및 사용 여부 확인 후, 쿠폰 정책 적용)
        validateCouponService.validate(command.userId(), command.couponId());

        // 1. 주문 생성 (상품 재고 차감 포함)
        Order order = createOrderService.execute(
                command.userId(),
                command.items(),
                command.couponId()
        );

        // 2. 주문 생성 이벤트 발행 (→ 포인트 차감 등의 후속 처리는 이벤트 리스너에서 수행)
        OrderRequestedEventData eventData = new OrderRequestedEventData(
                order.getId(),
                order.getUserId(),
                order.getFinalPrice(),
                command.items(),
                command.couponId()
        );
        orderEventPublisher.publishRequest(eventData);

        // 3. 판매 기록 이벤트 발행 (→ 상품별 판매량 저장, 인기상품 랭킹 갱신 등)
        productSaleEventPublisher.publishSale(order);

        // 4. 주문 결과 생성 (reporting용) 후 이벤트 발행 (→ 통계/리포트용 처리)
        /*OrderResponse response = new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                null,
                order.getOrderedAt()
        );
        reportEventPublisher.publishReport(response);*/

        OrderReportMessage message = new OrderReportMessage(
                order.getId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                order.getOrderedAt()
        );
        orderReportEventPublisher.publish(message);
    }

    @Transactional
    public OrderResult order(CreateOrderCommand command) {

        // 0. 쿠폰 처리 준비
        UserCoupon userCoupon = null;
        if (command.couponId() != null) {
            // 0-1. 사용자 보유 쿠폰 조회
            userCoupon = getUserCouponService.execute(command.userId(), command.couponId());

            // 0-2. 쿠폰 사용 여부 체크
            if (userCoupon.isUsed()) {
                throw new IllegalStateException("이미 사용한 쿠폰입니다.");
            }

            // 0-3. 쿠폰 상세 조회 (유효성 검증)
            getCouponService.execute(command.couponId());
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

        // 5-1. Redis 랭킹 반영 (AFTER COMMIT)
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                updateProductRankingService.increaseOrderCountForItems(command.items());
            }
        });

        // 6. 응답 생성
        OrderResult result = new OrderResult(
                order.getId(),
                order.getTotalPrice(),
                order.getDiscount(),
                order.getFinalPrice(),
                order.getOrderedAt(),
                (int) updated.point()
        );

//        reporter.send(order.toResponse((int) updated.point()));
        reportEventPublisher.publishReport(order.toResponse((int) updated.point()));

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

