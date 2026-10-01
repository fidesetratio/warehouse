package com.app.warehouse.controller;

import com.app.warehouse.dto.QuantityRequest;
import com.app.warehouse.dto.VariantRequest;
import com.app.warehouse.dto.VariantResponse;
import com.app.warehouse.service.VariantService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/variants")
public class VariantController {

    private final VariantService variantService;

    public VariantController(VariantService variantService) {
        this.variantService = variantService;
    }

    @PutMapping("/{id}")
    public VariantResponse update(@PathVariable Long id,
                                  @Valid @RequestBody VariantRequest request) {
        return variantService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        variantService.delete(id);
    }

    @PostMapping("/{id}/sell")
    public VariantResponse sell(@PathVariable Long id,
                                @Valid @RequestBody QuantityRequest request) {
        return variantService.sell(id, request);
    }

    @PostMapping("/{id}/stock")
    public VariantResponse addStock(@PathVariable Long id,
                                    @Valid @RequestBody QuantityRequest request) {
        return variantService.addStock(id, request);
    }
}
