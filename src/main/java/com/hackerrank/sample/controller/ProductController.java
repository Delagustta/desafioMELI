package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.CompareProductsRequest;
import com.hackerrank.sample.dto.ApiErrorResponse;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Products", description = "Operations for product catalog and item comparison")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all products for comparison")
    @ApiResponse(
            responseCode = "200",
            description = "Products returned",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))
    )
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a single product by id")
    @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping(value = "/products/compare", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Compare multiple products by ids")
    @ApiResponse(
            responseCode = "200",
            description = "Products returned in the same input order",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request payload",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "At least one product id was not found",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Unexpected server error",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
    )
    public List<ProductResponse> compareProducts(@RequestBody @Valid CompareProductsRequest request) {
        return productService.compareProducts(request.ids());
    }
}
