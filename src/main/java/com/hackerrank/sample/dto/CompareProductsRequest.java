package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(
        description = "Request to compare products by id. Response mirrors this list: same order and duplicate ids."
)
public record CompareProductsRequest(
        @Schema(
                description = "Product ids to compare; response preserves order and repeats the same product for repeated ids",
                example = "[101, 102, 103]"
        )
        @NotEmpty(message = "ids must not be empty")
        List<Long> ids
) {
}
