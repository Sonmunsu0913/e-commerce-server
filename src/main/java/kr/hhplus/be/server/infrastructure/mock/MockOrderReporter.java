package kr.hhplus.be.server.infrastructure.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;

@Component
public class MockOrderReporter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void send(OrderResponse orderResponse) {
        System.out.println("[✅ 외부 데이터 플랫폼 전송됨]");
        try {
            String json = objectMapper.writeValueAsString(orderResponse);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.out.println("[⚠️ 전송 실패] JSON 직렬화 에러");
            e.printStackTrace();
        }
    }
}
