package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.application.product.service.ProductStatisticsService;
import kr.hhplus.be.server.interfaces.api.product.dto.PopularProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/popular")
@Tag(name = "PopularProduct", description = "최근 3일간 판매량 기준으로 상위 5개 인기 상품을 조회합니다.")
public class ProductStatisticsController {

    private final ProductStatisticsService productStatisticsService;

    public ProductStatisticsController(ProductStatisticsService productStatisticsService) {
        this.productStatisticsService = productStatisticsService;
    }

    @GetMapping
    @Operation(
        summary = "인기 상품 조회",
        description = "range 쿼리 파라미터를 기준으로 최근 인기 상품을 조회합니다. 예: ?range=3d"
    )
    public ResponseEntity<List<PopularProductResponse>> getPopularProducts(
        @RequestParam(name = "range", defaultValue = "3d") String range) {

        List<PopularProductResponse> response = productStatisticsService.getPopularProducts(range);
        return ResponseEntity.ok(response);
    }

}
