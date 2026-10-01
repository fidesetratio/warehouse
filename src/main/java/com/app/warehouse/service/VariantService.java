package com.app.warehouse.service;

import com.app.warehouse.dto.QuantityRequest;
import com.app.warehouse.dto.VariantRequest;
import com.app.warehouse.dto.VariantResponse;
import com.app.warehouse.entity.Item;
import com.app.warehouse.entity.ItemVariant;
import com.app.warehouse.exception.DuplicateResourceException;
import com.app.warehouse.exception.InsufficientStockException;
import com.app.warehouse.exception.ResourceNotFoundException;
import com.app.warehouse.repository.ItemVariantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VariantService {

    private final ItemVariantRepository variantRepository;
    private final ItemService itemService;

    public VariantService(ItemVariantRepository variantRepository, ItemService itemService) {
        this.variantRepository = variantRepository;
        this.itemService = itemService;
    }

    public List<VariantResponse> findByItemId(Long itemId) {
        itemService.getItem(itemId);
        return variantRepository.findByItemId(itemId).stream()
                .map(this::toResponse)
                .toList();
    }

    public VariantResponse create(Long itemId, VariantRequest request) {
        Item item = itemService.getItem(itemId);

        if (variantRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException("SKU already exists: " + request.sku());
        }

        ItemVariant variant = new ItemVariant();
        variant.setItem(item);
        variant.setSku(request.sku());
        variant.setName(request.name());
        variant.setPrice(request.price());
        variant.setStock(request.stock());

        return toResponse(variantRepository.save(variant));
    }

    @Transactional
    public VariantResponse update(Long id, VariantRequest request) {
        ItemVariant variant = getVariant(id);

        if (!variant.getSku().equals(request.sku()) && variantRepository.existsBySku(request.sku())) {
            throw new DuplicateResourceException("SKU already exists: " + request.sku());
        }

        variant.setSku(request.sku());
        variant.setName(request.name());
        variant.setPrice(request.price());
        variant.setStock(request.stock());

        return toResponse(variant);
    }

    public void delete(Long id) {
        variantRepository.delete(getVariant(id));
    }

    @Transactional
    public VariantResponse sell(Long id, QuantityRequest request) {
        ItemVariant variant = getVariant(id);

        if (variant.getStock() < request.quantity()) {
            throw new InsufficientStockException(
                    "Insufficient stock. Available: " + variant.getStock()
            );
        }

        variant.setStock(variant.getStock() - request.quantity());
        return toResponse(variant);
    }

    @Transactional
    public VariantResponse addStock(Long id, QuantityRequest request) {
        ItemVariant variant = getVariant(id);
        variant.setStock(variant.getStock() + request.quantity());
        return toResponse(variant);
    }

    private ItemVariant getVariant(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Variant not found: " + id));
    }

    private VariantResponse toResponse(ItemVariant variant) {
        return new VariantResponse(
                variant.getId(),
                variant.getItem().getId(),
                variant.getSku(),
                variant.getName(),
                variant.getPrice(),
                variant.getStock()
        );
    }
}
