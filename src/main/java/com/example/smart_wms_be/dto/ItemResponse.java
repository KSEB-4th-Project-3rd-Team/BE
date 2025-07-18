package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemResponse {
    private Long itemId;
    private String itemCode;
    private String itemName;
    private String itemGroup;
    private String spec;
    private String unit;
    private Double unitPriceIn;
    private Double unitPriceOut;

    public static ItemResponse fromEntity(Item item) {
        return ItemResponse.builder()
                .itemId(item.getId())
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemGroup(item.getItemGroup())
                .spec(item.getSpec())
                .unit(item.getUnit())
                .unitPriceIn(item.getUnitPriceIn())
                .unitPriceOut(item.getUnitPriceOut())
                .build();
    }
}
