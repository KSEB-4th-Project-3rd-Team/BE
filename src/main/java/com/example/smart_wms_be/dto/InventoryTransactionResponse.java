package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.InventoryTransaction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InventoryTransactionResponse {
    private Long transactionId;
    private String itemName;
    private Integer quantity;
    private String transactionType;
    private LocalDateTime transactionTime;

    public static InventoryTransactionResponse fromEntity(InventoryTransaction tx) {
        return InventoryTransactionResponse.builder()
                .transactionId(tx.getId())
                .itemName(tx.getItem().getItemName())
                .quantity(tx.getQuantity())
                .transactionType(tx.getTransactionType().name())
                .transactionTime(tx.getTransactionTime())
                .build();
    }
}
