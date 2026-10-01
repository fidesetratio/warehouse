package com.app.warehouse.dto;

import java.math.BigDecimal;

public record VariantResponse(
        Long id,
        Long itemId,
        String sku,
        String name,
        BigDecimal price,
        Integer stock
) {
}
