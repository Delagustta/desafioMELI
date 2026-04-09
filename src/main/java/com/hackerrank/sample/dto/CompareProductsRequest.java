package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CompareProductsRequest(
        @Schema(
                description = "List of product ids to compare (order is preserved in the response)",
                example = "[101, 102, 103]"
        )
        @NotEmpty(message = "ids must not be empty")
        List<Long> ids
) {
}
