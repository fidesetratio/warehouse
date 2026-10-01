package com.app.warehouse.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record VariantRequest(
        @NotBlank @Size(max = 80) String sku,
        @NotBlank @Size(max = 150) String name,
        @NotNull @PositiveOrZero BigDecimal price,
        @NotNull @PositiveOrZero Integer stock
) {
}
