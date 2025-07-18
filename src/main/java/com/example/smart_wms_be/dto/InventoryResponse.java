package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Inventory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryResponse {
    private Long itemId;
    private String itemName;
    private String locationCode;
    private Integer quantity;
    private LocalDateTime lastUpdated;

    public static InventoryResponse fromEntity(Inventory inventory) {
        return InventoryResponse.builder()
                .itemId(inventory.getItem().getId())
                .itemName(inventory.getItem().getItemName())
                .locationCode(inventory.getLocationCode())
                .quantity(inventory.getQuantity())
                .lastUpdated(inventory.getLastUpdated())
                .build();
    }
}
