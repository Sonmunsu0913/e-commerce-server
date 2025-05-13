package kr.hhplus.be.server.infrastructure.scheduler;

import kr.hhplus.be.server.domain.product.service.GetTopSellingProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopularProductCacheScheduler {

    private final GetTopSellingProductService getTopSellingProductService;

    // 실제 스케줄링 대상
    @Scheduled(cron = "0 0 0 * * *")  // 매일 자정 실행
    public void refreshCache() {
        refreshManually();  // 분리된 메서드 호출
    }

    public void refresh() {
        getTopSellingProductService.refresh("1d");
        getTopSellingProductService.refresh("3d");
        getTopSellingProductService.refresh("7d");
    }

    // 테스트 가능하도록 public 메서드로 분리
    public void refreshManually() {
        getTopSellingProductService.execute("1d");
        getTopSellingProductService.execute("3d");
        getTopSellingProductService.execute("7d");
    }
}

