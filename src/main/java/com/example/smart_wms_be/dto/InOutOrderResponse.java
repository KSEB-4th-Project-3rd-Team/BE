package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class InOutOrderResponse {
    private Long orderId;
    private OrderType type;
    private String status;
    private String companyName;
    private String companyCode;
    private LocalDate expectedDate;
    private String locationCode; // 창고 구역 코드
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDto> items;
    
    @Getter
    @Builder
    public static class OrderItemDto {
        private Long itemId;
        private String itemName;
        private String itemCode;
        private String specification;
        private Integer requestedQuantity;
        private Integer processedQuantity;
    }
}
