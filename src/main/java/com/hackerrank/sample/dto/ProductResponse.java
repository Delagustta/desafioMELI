package com.hackerrank.sample.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ProductResponse(
        Long id,
        String name,
        String imageUrl,
        String description,
        BigDecimal price,
        Double rating,
        Map<String, String> specifications
) {
}
