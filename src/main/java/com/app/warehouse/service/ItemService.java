package com.app.warehouse.service;

import com.app.warehouse.dto.ItemRequest;
import com.app.warehouse.dto.ItemResponse;
import com.app.warehouse.entity.Item;
import com.app.warehouse.exception.ResourceNotFoundException;
import com.app.warehouse.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemResponse> findAll() {
        return itemRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ItemResponse findById(Long id) {
        return toResponse(getItem(id));
    }

    public ItemResponse create(ItemRequest request) {
        Item item = new Item();
        item.setName(request.name());
        item.setDescription(request.description());
        return toResponse(itemRepository.save(item));
    }

    @Transactional
    public ItemResponse update(Long id, ItemRequest request) {
        Item item = getItem(id);
        item.setName(request.name());
        item.setDescription(request.description());
        return toResponse(item);
    }

    public void delete(Long id) {
        Item item = getItem(id);
        itemRepository.delete(item);
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + id));
    }

    private ItemResponse toResponse(Item item) {
        return new ItemResponse(item.getId(), item.getName(), item.getDescription());
    }
}
