package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.Map;

public record ProductResponse(
        @Schema(description = "Unique product identifier", example = "101")
        Long id,
        @Schema(description = "Display name of the product", example = "Smartphone X")
        String name,
        @Schema(description = "Public URL for product image", example = "https://images.example.com/smartphone-x.jpg")
        String imageUrl,
        @Schema(description = "Short description used in product listing", example = "Smartphone with AMOLED display and 5G connectivity.")
        String description,
        @Schema(description = "Current product price", example = "2499.90")
        BigDecimal price,
        @Schema(description = "Product rating between 0 and 5", example = "4.7")
        Double rating,
        @Schema(
                description = "Technical specifications as key-value pairs",
                example = "{\"screen\":\"6.1 inch AMOLED\",\"storage\":\"256 GB\",\"battery\":\"4200 mAh\"}"
        )
        Map<String, String> specifications
) {
}
