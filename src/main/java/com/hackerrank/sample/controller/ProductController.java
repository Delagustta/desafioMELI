package com.hackerrank.sample.controller;

import com.hackerrank.sample.dto.CompareProductsRequest;
import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping(value = "/products/compare", consumes = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> compareProducts(@RequestBody @Valid CompareProductsRequest request) {
        return productService.compareProducts(request.ids());
    }
}
