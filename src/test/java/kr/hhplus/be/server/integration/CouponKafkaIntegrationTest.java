package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.domain.coupon.service.IssueCouponService;
import kr.hhplus.be.server.infrastructure.coupon.kafka.CouponIssueEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EnableKafka
@EmbeddedKafka(partitions = 1, topics = {"coupon.issue.request"})
class CouponKafkaIntegrationTest {

    @Autowired
    private CouponIssueEventPublisher publisher;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IssueCouponService issueCouponService; // 수동으로 주입됨

    @TestConfiguration
    static class MockConfig {
        @Bean
        public IssueCouponService issueCouponService() {
            return Mockito.mock(IssueCouponService.class);
        }
    }

    @Test
    void endToEnd_publishAndConsume_shouldInvokeIssueCouponService() throws InterruptedException {
        // given
        Long userId = 1L;
        Long couponId = 100L;

        // when
        publisher.publish(userId, couponId);

        // then
        verify(issueCouponService, timeout(2000)).issueCouponWithTx(userId, couponId);
    }

    @Test
    void issueCouponV3_shouldSendKafkaAndTriggerCouponIssuance() throws Exception {
        // given
        Long userId = 1L;
        Long couponId = 100L;

        // when
        mockMvc.perform(post("/api/coupon/v3/{userId}/coupon/{couponId}", userId, couponId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // then - Kafka listener가 consume하고, 실제 서비스가 호출되었는지 확인
        verify(issueCouponService, timeout(2000)).issueCouponWithTx(userId, couponId);
    }
}

