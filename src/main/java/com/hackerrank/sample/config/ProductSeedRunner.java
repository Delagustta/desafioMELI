package com.hackerrank.sample.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackerrank.sample.model.Product;
import com.hackerrank.sample.repository.ProductRepository;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ProductSeedRunner implements ApplicationRunner {
    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;
    private final String productsFile;

    public ProductSeedRunner(
            ProductRepository productRepository,
            ObjectMapper objectMapper,
            @Value("${app.seed.products-file:products.json}") String productsFile
    ) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
        this.productsFile = productsFile;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (productRepository.count() > 0) {
            return;
        }

        ClassPathResource resource = new ClassPathResource(productsFile);
        try (InputStream inputStream = resource.getInputStream()) {
            List<Product> products = objectMapper.readValue(inputStream, new TypeReference<List<Product>>() {});
            productRepository.saveAll(products);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load product seed data.", ex);
        }
    }
}
