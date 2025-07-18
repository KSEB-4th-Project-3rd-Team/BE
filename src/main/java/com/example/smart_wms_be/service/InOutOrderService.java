package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.*;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 입출고 주문 관련 비즈니스 로직 처리
 */
@Service
@RequiredArgsConstructor
public class InOutOrderService {

    private final InOutOrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CompanyRepository companyRepository;
    private final ItemRepository itemRepository;

    // 전체 주문 목록 조회 (필터 optional)
    public List<InOutOrderResponse> getOrders(OrderType type, OrderStatus status) {
        List<InOutOrder> orders = (type != null && status != null)
                ? orderRepository.findByTypeAndStatus(type, status)
                : orderRepository.findAll();

        return orders.stream().map(order -> InOutOrderResponse.builder()
                .orderId(order.getId())
                .type(order.getType())
                .status(order.getStatus().name())
                .companyName(order.getCompany().getCompanyName())
                .expectedDate(order.getExpectedDate())
                .build()).collect(Collectors.toList());
    }

    // 주문 생성
    public InOutOrderResponse createOrder(InOutOrderRequest request) {
        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new IllegalArgumentException("거래처 없음"));

        InOutOrder order = InOutOrder.builder()
                .type(request.getType())
                .status(OrderStatus.PENDING)
                .expectedDate(request.getExpectedDate())
                .company(company)
                .build();

        List<OrderItem> items = request.getItems().stream().map(dto -> {
            Item item = itemRepository.findById(dto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("품목 없음"));
            return OrderItem.builder()
                    .item(item)
                    .requestedQuantity(dto.getQuantity())
                    .processedQuantity(0)
                    .order(order)
                    .build();
        }).collect(Collectors.toList());

        order.setItems(items);
        orderRepository.save(order); // cascade로 OrderItem도 저장됨

        return InOutOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .build();
    }

    // 단일 주문 상세 조회
    public InOutOrder getOrderDetail(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));
    }

    // 상태 변경
    public InOutOrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {
        InOutOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));
        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return InOutOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .type(order.getType())
                .companyName(order.getCompany().getCompanyName())
                .expectedDate(order.getExpectedDate())
                .build();
    }
}
