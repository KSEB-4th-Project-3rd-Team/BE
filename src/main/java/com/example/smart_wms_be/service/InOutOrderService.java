package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.*;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    private final InventoryRepository inventoryRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;

    // 전체 주문 목록 조회 (필터 optional) - 최신순 정렬
    @Transactional(readOnly = true)
    public List<InOutOrderResponse> getOrders(OrderType type, OrderStatus status) {
        List<InOutOrder> orders = (type != null && status != null)
                ? orderRepository.findByTypeAndStatus(type, status)
                : orderRepository.findAllByOrderByIdDesc();

        return orders.stream().map(order -> InOutOrderResponse.builder()
                .orderId(order.getId())
                .type(order.getType())
                .status(order.getStatus().name())
                .companyName(order.getCompany().getCompanyName())
                .companyCode(order.getCompany().getCompanyCode())
                .expectedDate(order.getExpectedDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream().map(item -> 
                    InOutOrderResponse.OrderItemDto.builder()
                        .itemId(item.getItem().getId())
                        .itemName(item.getItem().getItemName())
                        .itemCode(item.getItem().getItemCode())
                        .specification(item.getItem().getSpec())
                        .requestedQuantity(item.getRequestedQuantity())
                        .processedQuantity(item.getProcessedQuantity())
                        .build()
                ).collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    // 주문 생성
    @Transactional
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
                .type(order.getType())
                .companyName(order.getCompany().getCompanyName())
                .companyCode(order.getCompany().getCompanyCode())
                .expectedDate(order.getExpectedDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream().map(item -> 
                    InOutOrderResponse.OrderItemDto.builder()
                        .itemId(item.getItem().getId())
                        .itemName(item.getItem().getItemName())
                        .itemCode(item.getItem().getItemCode())
                        .specification(item.getItem().getSpec())
                        .requestedQuantity(item.getRequestedQuantity())
                        .processedQuantity(item.getProcessedQuantity())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }

    // 단일 주문 상세 조회
    public InOutOrder getOrderDetail(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));
    }

    // 상태 변경
    @Transactional
    public InOutOrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {
        InOutOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));
        
        OrderStatus oldStatus = order.getStatus();
        order.setStatus(request.getStatus());
        orderRepository.save(order);

        // COMPLETED 상태로 변경될 때 재고 업데이트
        if (request.getStatus() == OrderStatus.COMPLETED && oldStatus != OrderStatus.COMPLETED) {
            updateInventoryForCompletedOrder(order);
        }

        return InOutOrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .type(order.getType())
                .companyName(order.getCompany().getCompanyName())
                .companyCode(order.getCompany().getCompanyCode())
                .expectedDate(order.getExpectedDate())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(order.getItems().stream().map(item -> 
                    InOutOrderResponse.OrderItemDto.builder()
                        .itemId(item.getItem().getId())
                        .itemName(item.getItem().getItemName())
                        .itemCode(item.getItem().getItemCode())
                        .specification(item.getItem().getSpec())
                        .requestedQuantity(item.getRequestedQuantity())
                        .processedQuantity(item.getProcessedQuantity())
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }

    // 완료된 주문에 대해 재고 업데이트
    private void updateInventoryForCompletedOrder(InOutOrder order) {
        for (OrderItem orderItem : order.getItems()) {
            Item item = orderItem.getItem();
            Integer quantity = orderItem.getRequestedQuantity();
            String locationCode = "A-01"; // 기본 위치
            
            // 기존 재고 찾기 또는 새로 생성
            Optional<Inventory> existingInventory = inventoryRepository
                    .findByItemAndLocationCode(item, locationCode);
            
            Inventory inventory;
            if (existingInventory.isPresent()) {
                inventory = existingInventory.get();
                if (order.getType() == OrderType.INBOUND) {
                    inventory.setQuantity(inventory.getQuantity() + quantity);
                } else if (order.getType() == OrderType.OUTBOUND) {
                    inventory.setQuantity(Math.max(0, inventory.getQuantity() - quantity));
                }
                inventory.setLastUpdated(LocalDateTime.now());
            } else {
                // 새 재고 생성 (입고의 경우만)
                if (order.getType() == OrderType.INBOUND) {
                    inventory = Inventory.builder()
                            .item(item)
                            .locationCode(locationCode)
                            .quantity(quantity)
                            .lastUpdated(LocalDateTime.now())
                            .build();
                } else {
                    continue; // 출고인데 재고가 없으면 스킵
                }
            }
            
            inventoryRepository.save(inventory);
            
            // 재고 거래 내역 기록
            InventoryTransaction transaction = InventoryTransaction.builder()
                    .item(item)
                    .transactionType(order.getType() == OrderType.INBOUND ? 
                        TransactionType.INBOUND : TransactionType.OUTBOUND)
                    .quantity(quantity)
                    .transactionTime(LocalDateTime.now())
                    .build();
            
            inventoryTransactionRepository.save(transaction);
        }
    }
}
