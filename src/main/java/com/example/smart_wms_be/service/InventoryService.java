package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Inventory;
import com.example.smart_wms_be.domain.InventoryTransaction;
import com.example.smart_wms_be.dto.InventoryResponse;
import com.example.smart_wms_be.dto.InventoryTransactionResponse;
import com.example.smart_wms_be.repository.InventoryRepository;
import com.example.smart_wms_be.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 재고 관리 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository transactionRepository;

    public List<InventoryResponse> getInventory(String itemCode, String locationCode) {
        // 필터가 있으면 필터링, 없으면 전체 조회 (최신순)
        List<Inventory> inventories = (itemCode != null && !itemCode.isEmpty()) || 
                                     (locationCode != null && !locationCode.isEmpty())
                ? inventoryRepository.findByItem_ItemCodeContainingAndLocationCodeContaining(
                        itemCode == null ? "" : itemCode,
                        locationCode == null ? "" : locationCode)
                : inventoryRepository.findAllByOrderByIdDesc();
                
        return inventories.stream()
                .map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InventoryTransactionResponse> getHistory() {
        return transactionRepository.findAllByOrderByIdDesc().stream()
                .map(InventoryTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
