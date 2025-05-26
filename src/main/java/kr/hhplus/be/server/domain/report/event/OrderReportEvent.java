package kr.hhplus.be.server.domain.report.event;

import kr.hhplus.be.server.interfaces.api.order.OrderResponse;

public class OrderReportEvent {
    private final OrderResponse response;

    public OrderReportEvent(OrderResponse response) {
        this.response = response;
    }

    public OrderResponse getResponse() {
        return response;
    }
}