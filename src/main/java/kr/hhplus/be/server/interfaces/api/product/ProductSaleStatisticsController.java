package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.application.product.usecase.GetTopSellingProductsUseCase;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/sale/statistics")
@Tag(name = "ProductSaleStatistics", description = "상품 판매 통계 관련 API")
public class ProductSaleStatisticsController {

    private final GetTopSellingProductsUseCase getTopSellingProductsUseCase;

    public ProductSaleStatisticsController(GetTopSellingProductsUseCase getTopSellingProductsUseCase) {
        this.getTopSellingProductsUseCase = getTopSellingProductsUseCase;
    }

    @GetMapping("/popular")
    @Operation(
            summary = "인기 상품 조회",
            description = "range 쿼리 파라미터를 기준으로 최근 인기 상품을 조회합니다. 예: ?range=3d"
    )
    public ResponseEntity<List<PopularProductResponse>> getPopularProducts(
            @Parameter(description = "조회 기간 범위 (예: 3d는 최근 3일)")
            @RequestParam(name = "range", defaultValue = "3d") String range
    ) {
        List<PopularProductResponse> response = getTopSellingProductsUseCase.execute(range);
        return ResponseEntity.ok(response);
    }

}
