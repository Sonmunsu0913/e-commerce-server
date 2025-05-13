package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.domain.order.OrderItemCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderRankingIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String rankingKey;

    @BeforeEach
    void setup() {
        // 일간 랭킹 키 초기화
        String today = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        rankingKey = "product:order:ranking:" + today;
        redisTemplate.delete(rankingKey); // 기존 데이터 제거
    }

    @Test
    void 주문_시_상품_랭킹이_Redis에_누적_반영된다() {
        // given
        Long userId = 1L;
        Long productId = 1L;
        int quantity1 = 3;
        int quantity2 = 5;
        CreateOrderCommand firstOrder = new CreateOrderCommand(
            userId,
            List.of(new OrderItemCommand(productId, "상품A", 1000, quantity1)),
            null
        );

        CreateOrderCommand secondOrder = new CreateOrderCommand(
            userId,
            List.of(new OrderItemCommand(productId, "상품A", 1000, quantity2)),
            null
        );

        // when
        orderFacade.order(firstOrder);
        Double scoreAfterFirst = redisTemplate.opsForZSet().score(rankingKey, productId.toString());
        System.out.println("1111 주문 1회차 후 score = " + scoreAfterFirst);

        orderFacade.order(secondOrder);
        Double scoreAfterSecond = redisTemplate.opsForZSet().score(rankingKey, productId.toString());
        System.out.println("2222 주문 2회차 후 score = " + scoreAfterSecond);

        // then
        System.out.println("Redis 랭킹 키 = " + rankingKey);
        System.out.println("Redis 최종 점수: productId = " + productId + ", score = " + scoreAfterSecond);

        assertThat(scoreAfterFirst).isEqualTo((double) quantity1);
        assertThat(scoreAfterSecond).isEqualTo((double) (quantity1 + quantity2));
    }

}
