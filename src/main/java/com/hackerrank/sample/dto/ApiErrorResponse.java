package com.hackerrank.sample.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.ZonedDateTime;
import java.util.List;

public record ApiErrorResponse(
        @Schema(
                description = "Error timestamp in America/Sao_Paulo (no fractional seconds)",
                example = "2026-04-09T22:15:30-03:00"
        )
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "America/Sao_Paulo")
        ZonedDateTime timestamp,
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
