package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchResourceFoundException("Product with given id not found."));

        return toResponse(product);
    }

    @Override
    public List<ProductResponse> compareProducts(List<Long> ids) {
        Map<Long, Product> foundById = productRepository.findAllById(ids).stream()
                .collect(LinkedHashMap::new, (map, product) -> map.put(product.getId(), product), Map::putAll);

        if (foundById.size() != ids.size()) {
            throw new NoSuchResourceFoundException("At least one requested product was not found.");
        }

        return ids.stream()
                .map(foundById::get)
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getDescription(),
                product.getPrice(),
                product.getRating(),
                product.getSpecifications()
        );
    }
}
