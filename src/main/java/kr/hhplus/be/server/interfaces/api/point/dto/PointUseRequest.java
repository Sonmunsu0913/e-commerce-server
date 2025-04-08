package kr.hhplus.be.server.interfaces.api.point.dto;

public class PointUseRequest {
    private Long userId;
    private Integer amount;

    public Long getUserId() { return userId; }
    public Integer getAmount() { return amount; }
}

