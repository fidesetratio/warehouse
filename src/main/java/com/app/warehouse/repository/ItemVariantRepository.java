package com.app.warehouse.repository;

import com.app.warehouse.entity.ItemVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemVariantRepository extends JpaRepository<ItemVariant, Long> {

    List<ItemVariant> findByItemId(Long itemId);

    boolean existsBySku(String sku);
}
