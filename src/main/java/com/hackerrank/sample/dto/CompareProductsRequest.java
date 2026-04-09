package com.hackerrank.sample.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CompareProductsRequest(
        @NotEmpty(message = "ids must not be empty")
        List<Long> ids
) {
}
