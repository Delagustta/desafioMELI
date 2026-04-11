package com.hackerrank.sample.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductSeedRunner implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.seed.products-file:products.json}")
    private String productsFile;

    @Override
    public void run(ApplicationArguments args) {
        long existing = productRepository.count();
        if (existing > 0) {
            log.info("Product seed skipped: database already contains {} row(s)", existing);
            return;
        }

        ClassPathResource resource = new ClassPathResource(productsFile);
        try (InputStream inputStream = resource.getInputStream()) {
            List<Product> products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            productRepository.saveAll(products);
            log.info("Product seed loaded from classpath:{} count={}", productsFile, products.size());
        } catch (IOException ex) {
            log.error("Failed to read product seed file classpath:{}", productsFile, ex);
            throw new IllegalStateException("Could not load product seed data.", ex);
        }
    }
}
