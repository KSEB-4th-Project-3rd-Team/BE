package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item", description = "품목 관리 API")
public class ItemController {

    private final ItemService itemService;

    // 전체 품목 목록 조회
    @GetMapping
    public List<ItemResponse> getItems() {
        long startTime = System.currentTimeMillis();
        List<ItemResponse> result = itemService.getAllItems();
        long endTime = System.currentTimeMillis();
        System.out.println("🚀 ItemController.getItems() 실행시간: " + (endTime - startTime) + "ms, 결과 개수: " + result.size());
        return result;
    }

    // 품목 등록
    @PostMapping
    public ItemResponse createItem(@RequestBody CreateItemRequest request) {
        return itemService.createItem(request);
    }

    // 품목 정보 수정
    @PutMapping("/{itemId}")
    public ItemResponse updateItem(@PathVariable Long itemId,
                                   @RequestBody UpdateItemRequest request) {
        return itemService.updateItem(itemId, request);
    }

    // 품목 삭제
    @DeleteMapping("/{itemId}")
    public MessageResponse deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new MessageResponse("Item deleted successfully");
    }
}
