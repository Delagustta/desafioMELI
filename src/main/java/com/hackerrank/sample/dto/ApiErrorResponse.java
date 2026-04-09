package com.hackerrank.sample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

public record ApiErrorResponse(
        @Schema(description = "Error timestamp in UTC", example = "2026-04-09T03:35:48.123Z")
        Instant timestamp,
        @Schema(description = "HTTP status code", example = "404")
        int status,
        @Schema(description = "HTTP status reason phrase", example = "Not Found")
        String error,
        @Schema(description = "Human-readable error summary", example = "Product with given id not found.")
        String message,
        @Schema(description = "Request path that originated the error", example = "/products/999")
        String path,
        @Schema(description = "Optional validation details list", example = "[\"ids: must not be empty\"]")
        List<String> details
) {
}
