package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.BadResourceRequestException;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products.isEmpty()) {
            log.warn("Product catalog is empty");
            return List.of();
        }

        return products.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.debug("Product not found id={}", id);
                    return new NoSuchResourceFoundException("Product with given id not found.");
                });

        return toResponse(product);
    }

    @Override
    public List<ProductResponse> compareProducts(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            throw new BadResourceRequestException("ids must not be null or empty");
        }

        List<Long> distinctIds = ids.stream().distinct().toList();

        Map<Long, Product> foundById = productRepository.findAllById(distinctIds)
                .stream()
                .collect(Collectors.toMap(
                        Product::getId,
                        Function.identity()
                ));

        if (foundById.size() != distinctIds.size()) {
            List<Long> missingIds = distinctIds.stream()
                    .filter(id -> !foundById.containsKey(id))
                    .toList();

            log.debug("Compare rejected: missingIds={}", missingIds);

            throw new NoSuchResourceFoundException("Some products were not found: " + missingIds);
        }

        return ids.stream()
                .map(foundById::get)
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        Objects.requireNonNull(product, "product must not be null");
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
