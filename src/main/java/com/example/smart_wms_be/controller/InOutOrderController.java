package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.domain.InOutOrder;
import com.example.smart_wms_be.domain.OrderStatus;
import com.example.smart_wms_be.domain.OrderType;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.service.InOutOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 입출고 주문 API 컨트롤러
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inout/orders")
@Tag(name = "In Out Order", description = "입출고 주문 관리 API")
public class InOutOrderController {

    private final InOutOrderService inOutOrderService;

    @GetMapping
    public List<InOutOrderResponse> getOrders(
            @RequestParam(required = false) OrderType type,
            @RequestParam(required = false) OrderStatus status
    ) {
        return inOutOrderService.getOrders(type, status);
    }

    @PostMapping
    public InOutOrderResponse createOrder(@RequestBody InOutOrderRequest request) {
        return inOutOrderService.createOrder(request);
    }

    @GetMapping("/{orderId}")
    public InOutOrder getOrderDetail(@PathVariable Long orderId) {
        return inOutOrderService.getOrderDetail(orderId);
    }

    @PutMapping("/{orderId}/status")
    public InOutOrderResponse updateStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request
    ) {
        return inOutOrderService.updateStatus(orderId, request);
    }
}
