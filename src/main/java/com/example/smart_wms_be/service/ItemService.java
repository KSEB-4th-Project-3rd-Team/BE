package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Item;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 전체 품목 조회
    public List<ItemResponse> getAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 품목 생성
    public ItemResponse createItem(CreateItemRequest request) {
        Item item = Item.builder()
                .itemCode(request.getItemCode())
                .itemName(request.getItemName())
                .itemGroup(request.getItemGroup())
                .spec(request.getSpec())
                .unit(request.getUnit())
                .unitPriceIn(request.getUnitPriceIn())
                .unitPriceOut(request.getUnitPriceOut())
                .build();

        return ItemResponse.fromEntity(itemRepository.save(item));
    }

    // 품목 수정
    public ItemResponse updateItem(Long itemId, UpdateItemRequest request) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        if (request.getItemCode() != null) {
            item.setItemCode(request.getItemCode());
        }
        if (request.getItemName() != null) {
            item.setItemName(request.getItemName());
        }
        if (request.getItemGroup() != null) {
            item.setItemGroup(request.getItemGroup());
        }
        if (request.getSpec() != null) {
            item.setSpec(request.getSpec());
        }
        if (request.getUnit() != null) {
            item.setUnit(request.getUnit());
        }
        if (request.getUnitPriceIn() != null) {
            item.setUnitPriceIn(request.getUnitPriceIn());
        }
        if (request.getUnitPriceOut() != null) {
            item.setUnitPriceOut(request.getUnitPriceOut());
        }

        return ItemResponse.fromEntity(itemRepository.save(item));
    }

    // 품목 삭제
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
