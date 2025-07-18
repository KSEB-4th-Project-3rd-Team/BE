package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.OrderType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class InOutOrderResponse {
    private Long orderId;
    private OrderType type;
    private String status;
    private String companyName;
    private LocalDate expectedDate;
}
