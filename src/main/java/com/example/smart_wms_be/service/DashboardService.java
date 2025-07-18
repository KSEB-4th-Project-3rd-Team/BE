package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.OrderStatus;
import com.example.smart_wms_be.domain.OrderType;
import com.example.smart_wms_be.domain.Amr;
import com.example.smart_wms_be.dto.DashboardSummaryResponse;
import com.example.smart_wms_be.repository.AmrRepository;
import com.example.smart_wms_be.repository.InOutOrderRepository;
import com.example.smart_wms_be.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 대시보드 요약 정보 계산 서비스
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InventoryRepository inventoryRepository;
    private final InOutOrderRepository orderRepository;
    private final AmrRepository amrRepository;

    public DashboardSummaryResponse getSummary() {
        // 전체 재고 가치 계산
        double totalValue = inventoryRepository.calculateTotalInventoryValue();

        // 입출고 상태별 대기 중 주문 수
        int inboundPending = orderRepository.countByTypeAndStatus(OrderType.INBOUND, OrderStatus.PENDING);
        int outboundPending = orderRepository.countByTypeAndStatus(OrderType.OUTBOUND, OrderStatus.PENDING);

        // AMR 상태 통계
        Map<String, Integer> amrStatus = new HashMap<>();
        for (Amr amr : amrRepository.findAll()) {
            String status = amr.getStatus().name().toLowerCase();
            amrStatus.put(status, amrStatus.getOrDefault(status, 0) + 1);
        }

        return DashboardSummaryResponse.builder()
                .totalInventoryValue(totalValue)
                .inboundPending(inboundPending)
                .outboundPending(outboundPending)
                .amrStatus(amrStatus)
                .build();
    }
}
