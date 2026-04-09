package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductResponse;
import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();

    ProductResponse getProductById(Long id);

    List<ProductResponse> compareProducts(List<Long> ids);
}
