package com.app.warehouse.controller;

import com.app.warehouse.dto.ItemRequest;
import com.app.warehouse.dto.ItemResponse;
import com.app.warehouse.dto.VariantRequest;
import com.app.warehouse.dto.VariantResponse;
import com.app.warehouse.service.ItemService;
import com.app.warehouse.service.VariantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;
    private final VariantService variantService;

    public ItemController(ItemService itemService, VariantService variantService) {
        this.itemService = itemService;
        this.variantService = variantService;
    }

    @GetMapping
    public List<ItemResponse> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/{id}")
    public ItemResponse findById(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@Valid @RequestBody ItemRequest request) {
        return itemService.create(request);
    }

    @PutMapping("/{id}")
    public ItemResponse update(@PathVariable Long id,
                               @Valid @RequestBody ItemRequest request) {
        return itemService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        itemService.delete(id);
    }

    @GetMapping("/{itemId}/variants")
    public List<VariantResponse> findVariants(@PathVariable Long itemId) {
        return variantService.findByItemId(itemId);
    }

    @PostMapping("/{itemId}/variants")
    @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse createVariant(@PathVariable Long itemId,
                                         @Valid @RequestBody VariantRequest request) {
        return variantService.create(itemId, request);
    }
}
