package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InOutOrderRequest {
    private OrderType type;
    private Long companyId;
    private LocalDate expectedDate;
    private String locationCode; // 창고 구역 코드
    private List<OrderItemDto> items;
}
