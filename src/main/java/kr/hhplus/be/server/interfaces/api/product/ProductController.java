package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import kr.hhplus.be.server.application.product.dto.ProductResponse;
import kr.hhplus.be.server.application.product.dto.TopProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "상품 관련 API")
public class ProductController {

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "전체 상품 리스트를 반환합니다.")
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<ProductResponse> products = List.of(
            new ProductResponse(1, "콜라", 1500, 10),
            new ProductResponse(2, "사이다", 1400, 5)
        );
        return ResponseEntity.ok(products);
    }

    @GetMapping("/top")
    @Operation(summary = "상위 상품 조회", description = "최근 3일간 가장 많이 팔린 상위 5개 상품을 조회합니다.")
    public ResponseEntity<List<TopProductResponse>> topProducts() {
        List<TopProductResponse> top = List.of(
            new TopProductResponse(1, "콜라", 100),
            new TopProductResponse(2, "사이다", 90),
            new TopProductResponse(2, "환타", 75),
            new TopProductResponse(2, "포카리스웨이트", 55),
            new TopProductResponse(2, "헛개수", 20)
        );
        return ResponseEntity.ok(top);
    }

}
