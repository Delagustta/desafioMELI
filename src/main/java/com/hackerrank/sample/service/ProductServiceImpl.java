package com.hackerrank.sample.service;

import com.hackerrank.sample.dto.ProductResponse;
import com.hackerrank.sample.exception.NoSuchResourceFoundException;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<ProductResponse> getAllProducts() {
        List<ProductResponse> list = productRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
        log.debug("Listed products count={}", list.size());
        return list;
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
        log.debug("Compare products requested idCount={}", ids.size());
        Map<Long, Product> foundById = productRepository.findAllById(ids).stream()
                .collect(LinkedHashMap::new, (map, product) -> map.put(product.getId(), product), Map::putAll);

        if (foundById.size() != ids.size()) {
            log.debug(
                    "Compare rejected: requested idCount={} foundCount={}",
                    ids.size(),
                    foundById.size()
            );
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
