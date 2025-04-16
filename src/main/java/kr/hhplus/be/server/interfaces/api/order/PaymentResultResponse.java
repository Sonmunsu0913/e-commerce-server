package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentResultResponse {

    @Schema(description = "주문 ID", example = "1001")
    private Long id;

    @Schema(description = "총 결제 금액", example = "12000")
    private Integer paidAmount;

    @Schema(description = "할인 금액", example = "2000")
    private Integer discountAmount;

    @Schema(description = "최종 결제 금액", example = "10000")
    private Integer finalPaidAmount;

    @Schema(description = "결제 후 사용자 잔액", example = "5000")
    private Integer pointAfterPayment;

    @Schema(description = "결제 시각", example = "2025-04-08T13:00:00")
    private LocalDateTime paidAt;

    public PaymentResultResponse(Long id, Integer paidAmount, Integer discountAmount,
        Integer finalPaidAmount, Integer pointAfterPayment, LocalDateTime paidAt) {
        this.id = id;
        this.paidAmount = paidAmount;
        this.discountAmount = discountAmount;
        this.finalPaidAmount = finalPaidAmount;
        this.pointAfterPayment = pointAfterPayment;
        this.paidAt = paidAt;
    }

}

