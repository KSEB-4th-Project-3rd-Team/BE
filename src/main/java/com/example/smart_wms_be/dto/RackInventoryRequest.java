package com.example.smart_wms_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RackInventoryRequest {
    private String rackCode;
    private Long itemId;
    private Integer quantity;
}