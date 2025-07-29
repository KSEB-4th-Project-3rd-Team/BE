package com.example.smart_wms_be.dto;

import lombok.Getter;

@Getter
public class UpdateItemRequest {
    private String itemCode;
    private String itemName;
    private String itemGroup;
    private String spec;
    private String unit;
    private Double unitPriceIn;
    private Double unitPriceOut;
}
