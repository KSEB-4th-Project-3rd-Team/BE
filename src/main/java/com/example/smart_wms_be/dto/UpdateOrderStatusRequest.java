package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
