package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.*;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.TopProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
@Tag(name = "Product", description = "상품 관련 API")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "전체 상품 리스트를 반환합니다.")
    public ResponseEntity<List<ProductResponse>> listProducts() {
        return ResponseEntity.ok(productService.getAllProducts());

    }

}
