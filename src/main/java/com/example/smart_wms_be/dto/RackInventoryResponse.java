package com.example.smart_wms_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RackInventoryResponse {
    private Long id;
    private String rackCode;
    private Long itemId;
    private String itemCode;
    private String itemName;
    private Integer quantity;
    private LocalDateTime lastUpdated;
}