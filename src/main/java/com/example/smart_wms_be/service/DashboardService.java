package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.*;
import com.example.smart_wms_be.dto.DashboardSummaryResponse;
import com.example.smart_wms_be.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 대시보드 요약 정보 계산 서비스
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InventoryRepository inventoryRepository;
    private final InOutOrderRepository orderRepository;
    private final AmrRepository amrRepository;
    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;

    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
                .inventorySummary(calculateInventorySummary())
                .workStatusSummary(calculateWorkStatusSummary())
                .inOutAnalysis(calculateInOutAnalysis())
                .amrAnalysis(calculateAmrAnalysis())
                .salesAnalysis(calculateSalesAnalysis())
                .build();
    }

    private DashboardSummaryResponse.InventorySummary calculateInventorySummary() {
        List<Inventory> inventories = inventoryRepository.findAll();
        
        int totalItems = inventories.size();
        int normalStockItems = 0;
        int lowStockItems = 0;
        int outOfStockItems = 0;
        int totalQuantity = 0;

        for (Inventory inventory : inventories) {
            totalQuantity += inventory.getQuantity();
            
            if (inventory.getQuantity() == 0) {
                outOfStockItems++;
            } else if (inventory.getQuantity() <= 10) {
                lowStockItems++;
            } else {
                normalStockItems++;
            }
        }

        return DashboardSummaryResponse.InventorySummary.builder()
                .totalItems(totalItems)
                .normalStockItems(normalStockItems)
                .lowStockItems(lowStockItems)
                .outOfStockItems(outOfStockItems)
                .totalQuantity(totalQuantity)
                .build();
    }

    private DashboardSummaryResponse.WorkStatusSummary calculateWorkStatusSummary() {
        LocalDate today = LocalDate.now();
        
        int completedToday = orderRepository.countByStatusAndExpectedDate(OrderStatus.COMPLETED, today);
        int inProgressToday = orderRepository.countByStatusAndExpectedDate(OrderStatus.PENDING, today);
        int pendingToday = orderRepository.countByStatusAndExpectedDate(OrderStatus.PENDING, today);

        return DashboardSummaryResponse.WorkStatusSummary.builder()
                .completedToday(completedToday)
                .inProgressToday(0) // 현재는 진행 중 상태가 없음
                .pendingToday(pendingToday)
                .build();
    }

    private DashboardSummaryResponse.InOutAnalysis calculateInOutAnalysis() {
        int totalInbound = orderRepository.countByType(OrderType.INBOUND);
        int totalOutbound = orderRepository.countByType(OrderType.OUTBOUND);
        int completedInbound = orderRepository.countByTypeAndStatus(OrderType.INBOUND, OrderStatus.COMPLETED);
        int completedOutbound = orderRepository.countByTypeAndStatus(OrderType.OUTBOUND, OrderStatus.COMPLETED);
        
        double completionRate = totalInbound + totalOutbound > 0 ? 
            (double)(completedInbound + completedOutbound) / (totalInbound + totalOutbound) * 100 : 0;

        // 간단한 차트 데이터 생성
        List<DashboardSummaryResponse.ChartData> chartData = new ArrayList<>();
        chartData.add(DashboardSummaryResponse.ChartData.builder().name("입고").value(totalInbound).build());
        chartData.add(DashboardSummaryResponse.ChartData.builder().name("출고").value(totalOutbound).build());

        return DashboardSummaryResponse.InOutAnalysis.builder()
                .totalInbound(totalInbound)
                .totalOutbound(totalOutbound)
                .completionRate(completionRate)
                .chartData(chartData)
                .build();
    }

    private DashboardSummaryResponse.AmrAnalysis calculateAmrAnalysis() {
        List<Amr> amrs = amrRepository.findAll();
        
        int totalAmrs = amrs.size();
        int activeAmrs = 0;
        int errorAmrs = 0;
        
        Map<String, Integer> statusCount = new HashMap<>();
        
        for (Amr amr : amrs) {
            String status = amr.getStatus().name().toLowerCase();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
            
            if ("active".equals(status) || "moving".equals(status)) {
                activeAmrs++;
            } else if ("error".equals(status)) {
                errorAmrs++;
            }
        }

        List<DashboardSummaryResponse.ChartData> statusDistribution = statusCount.entrySet().stream()
                .map(entry -> DashboardSummaryResponse.ChartData.builder()
                        .name(entry.getKey())
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());

        return DashboardSummaryResponse.AmrAnalysis.builder()
                .totalAmrs(totalAmrs)
                .activeAmrs(activeAmrs)
                .errorAmrs(errorAmrs)
                .statusDistribution(statusDistribution)
                .build();
    }

    private DashboardSummaryResponse.SalesAnalysis calculateSalesAnalysis() {
        // 매출 분석은 현재 데이터가 없으므로 기본값 반환
        List<Company> companies = companyRepository.findAll();
        
        // 거래처별 주문 수를 기반으로 간단한 분석
        List<DashboardSummaryResponse.ChartData> companySalesDistribution = companies.stream()
                .map(company -> {
                    int orderCount = orderRepository.countByCompany(company);
                    return DashboardSummaryResponse.ChartData.builder()
                            .name(company.getCompanyName())
                            .value(orderCount)
                            .build();
                })
                .filter(data -> data.getValue() > 0)
                .collect(Collectors.toList());

        // 기본 매출 추이 데이터
        List<DashboardSummaryResponse.ChartData> salesTrend = new ArrayList<>();
        salesTrend.add(DashboardSummaryResponse.ChartData.builder().name("1월").value(1000).build());
        salesTrend.add(DashboardSummaryResponse.ChartData.builder().name("2월").value(1200).build());
        salesTrend.add(DashboardSummaryResponse.ChartData.builder().name("3월").value(800).build());

        return DashboardSummaryResponse.SalesAnalysis.builder()
                .totalSalesAmount(3000.0)
                .totalSalesCount(15)
                .companySalesDistribution(companySalesDistribution)
                .salesTrend(salesTrend)
                .build();
    }
}
