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
        return inventoryRepository
                .findByItem_ItemCodeContainingAndLocationCodeContaining(
                        itemCode == null ? "" : itemCode,
                        locationCode == null ? "" : locationCode
                ).stream().map(InventoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<InventoryTransactionResponse> getHistory() {
        return transactionRepository.findAll().stream()
                .map(InventoryTransactionResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
