package kr.hhplus.be.server.application.point.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class BalanceResponse {

    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    @Schema(description = "현재 잔액", example = "60000")
    private Integer balance;

    public BalanceResponse(Long userId, Integer balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getBalance() {
        return balance;
    }
}

