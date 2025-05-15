package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.product.service.GetDailyProductRankingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/ranking")
@Tag(name = "ProductRanking", description = "일간 상품 랭킹 조회 API")
public class ProductRankingController {

    private final GetDailyProductRankingService getDailyProductRankingService;

    public ProductRankingController(GetDailyProductRankingService getDailyProductRankingService) {
        this.getDailyProductRankingService = getDailyProductRankingService;
    }

    @GetMapping
    @Operation(summary = "일간 상품 랭킹 조회", description = "날짜 기준으로 Redis에 저장된 랭킹 조회")
    public ResponseEntity<List<ProductRankingResponse>> getRanking(
        @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyyMMdd") LocalDate date,
        @RequestParam(name = "limit", defaultValue = "10") int limit
    ) {
        List<ProductRankingResponse> response = getDailyProductRankingService.getRanking(date, limit);
        return ResponseEntity.ok(response);
    }
}
