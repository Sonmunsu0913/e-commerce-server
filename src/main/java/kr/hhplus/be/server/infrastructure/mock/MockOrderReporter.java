package kr.hhplus.be.server.infrastructure.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.hhplus.be.server.infrastructure.report.kafka.OrderReportMessage;
import kr.hhplus.be.server.interfaces.api.order.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockOrderReporter {

    private static final Logger log = LoggerFactory.getLogger(MockOrderReporter.class);
    private final ObjectMapper objectMapper;

    public MockOrderReporter() {
        this.objectMapper = new ObjectMapper();
        // LocalDateTime 직렬화 가능하도록 모듈 등록
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void send(OrderResponse orderResponse) {
        try {
            String message = objectMapper.writeValueAsString(orderResponse);
            log.info("[외부 데이터 플랫폼 전송됨] {}", message);
        } catch (JsonProcessingException e) {
            log.warn("[전송 실패] JSON 직렬화 에러", e);
        }
    }

    public void sendMessage(OrderReportMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            log.info("[외부 데이터 플랫폼 전송됨] {}", message);
        } catch (JsonProcessingException e) {
            log.warn("[전송 실패] JSON 직렬화 에러", e);
        }
    }
}
