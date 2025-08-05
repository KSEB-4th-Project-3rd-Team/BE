package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.InventoryResponse;
import com.example.smart_wms_be.dto.InventoryTransactionResponse;
import com.example.smart_wms_be.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 재고 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "재고 관리 API")
public class InventoryController {

    private final InventoryService inventoryService;

    // 재고 조회
    @GetMapping
    public List<InventoryResponse> getInventory(
            @RequestParam(required = false) String itemCode,
            @RequestParam(required = false) String locationCode
    ) {
        long startTime = System.currentTimeMillis();
        List<InventoryResponse> result = inventoryService.getInventory(itemCode, locationCode);
        long endTime = System.currentTimeMillis();
        System.out.println("🚀 InventoryController.getInventory() 실행시간: " + (endTime - startTime) + "ms, 결과 개수: " + result.size());
        return result;
    }

    // 재고 이력 조회
    @GetMapping("/history")
    public List<InventoryTransactionResponse> getHistory() {
        return inventoryService.getHistory();
    }
}
