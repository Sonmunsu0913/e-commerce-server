package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.domain.product.service.GetTopSellingProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/sale/statistics")
@Tag(name = "ProductSaleStatistics", description = "상품 판매 통계 관련 API")
public class ProductSaleStatisticsController {

    private final GetTopSellingProductService getTopSellingProductService;

    public ProductSaleStatisticsController(GetTopSellingProductService getTopSellingProductService) {
        this.getTopSellingProductService = getTopSellingProductService;
    }

    @GetMapping("/ranking/db")
    @Operation(
            summary = "인기 상품 조회",
            description = "range 쿼리 파라미터를 기준으로 최근 인기 상품을 조회합니다. 예: ?range=3d"
    )
    public ResponseEntity<List<PopularProductResponse>> getPopularProducts(
            @Parameter(description = "조회 기간 범위 (예: 3d는 최근 3일)")
            @RequestParam(name = "range", defaultValue = "3d") String range
    ) {
        List<PopularProductResponse> response = getTopSellingProductService.getFromDb(range);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ranking/redis")
    @Operation(
        summary = "최근 N일 인기 상품 조회 (Redis 기반)",
        description = "range 파라미터 기준으로 Redis ZUNIONSTORE 기반 인기 상품 랭킹을 조회합니다. 예: ?range=3d"
    )
    public ResponseEntity<List<ProductRankingResponse>> getPopularProductsFromRedis(
        @Parameter(description = "조회 범위 (예: 3d는 최근 3일)")
        @RequestParam(name = "range", defaultValue = "3d") String range
    ) {
        List<ProductRankingResponse> ranking = getTopSellingProductService.getFromRedis(range);
        return ResponseEntity.ok(ranking);
    }

}
