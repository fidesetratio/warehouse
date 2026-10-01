package com.app.warehouse.service;

import com.app.warehouse.dto.QuantityRequest;
import com.app.warehouse.entity.Item;
import com.app.warehouse.entity.ItemVariant;
import com.app.warehouse.exception.InsufficientStockException;
import com.app.warehouse.repository.ItemVariantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VariantServiceTest {

    @Mock
    private ItemVariantRepository variantRepository;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private VariantService variantService;

    @Test
    void sellShouldReduceStock() {
        Item item = new Item();
        ItemVariant variant = variant(10);
        variant.setItem(item);

        when(variantRepository.findById(1L)).thenReturn(Optional.of(variant));

        var response = variantService.sell(1L, new QuantityRequest(3));

        assertEquals(7, response.stock());
        assertEquals(7, variant.getStock());
    }

    @Test
    void sellShouldRejectWhenStockIsInsufficient() {
        Item item = new Item();
        ItemVariant variant = variant(2);
        variant.setItem(item);

        when(variantRepository.findById(1L)).thenReturn(Optional.of(variant));

        assertThrows(
                InsufficientStockException.class,
                () -> variantService.sell(1L, new QuantityRequest(3))
        );

        assertEquals(2, variant.getStock());
    }

    private ItemVariant variant(int stock) {
        ItemVariant variant = new ItemVariant();
        variant.setSku("SKU-001");
        variant.setName("Default");
        variant.setPrice(BigDecimal.valueOf(100));
        variant.setStock(stock);
        return variant;
    }
}
