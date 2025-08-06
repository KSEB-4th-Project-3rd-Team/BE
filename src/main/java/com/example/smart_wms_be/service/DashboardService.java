package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.*;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.*;
import com.example.smart_wms_be.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
    
    // 다른 서비스들을 주입받아 병렬 처리에 활용
    private final ItemService itemService;
    private final UserService userService;
    private final InOutOrderService inOutOrderService;
    private final InventoryService inventoryService;
    private final ScheduleService scheduleService;

    public DashboardSummaryResponse getSummary() {
        return DashboardSummaryResponse.builder()
                .inventorySummary(calculateInventorySummary())
                .workStatusSummary(calculateWorkStatusSummary())
                .inOutAnalysis(calculateInOutAnalysis())
                .amrAnalysis(calculateAmrAnalysis())
                .salesAnalysis(calculateSalesAnalysis())
                .build();
    }

    // 🚀 병렬 처리로 모든 대시보드 데이터를 한 번에 가져오는 메서드
    public DashboardDataResponse getAllDashboardData() {
        long totalStartTime = System.currentTimeMillis();
        
        // 각 API 호출을 CompletableFuture로 병렬 실행
        CompletableFuture<List<ItemResponse>> itemsFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            List<ItemResponse> result = itemService.getAllItems();
            long end = System.currentTimeMillis();
            System.out.println("⚡ Items 병렬 로딩: " + (end - start) + "ms");
            return result;
        });
        
        CompletableFuture<List<UserResponse>> usersFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            List<UserResponse> result = userService.getAllUsers();
            long end = System.currentTimeMillis();
            System.out.println("⚡ Users 병렬 로딩: " + (end - start) + "ms");
            return result;
        });
        
        CompletableFuture<List<InOutOrderResponse>> ordersFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            List<InOutOrderResponse> result = inOutOrderService.getOrders(null, null);
            long end = System.currentTimeMillis();
            System.out.println("⚡ Orders 병렬 로딩: " + (end - start) + "ms");
            return result;
        });
        
        CompletableFuture<List<InventoryResponse>> inventoryFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            List<InventoryResponse> result = inventoryService.getInventory(null, null);
            long end = System.currentTimeMillis();
            System.out.println("⚡ Inventory 병렬 로딩: " + (end - start) + "ms");
            return result;
        });
        
        CompletableFuture<List<ScheduleResponse>> schedulesFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            LocalDateTime startDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endDate = startDate.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
            List<ScheduleResponse> result = scheduleService.getSchedules(startDate, endDate);
            long end = System.currentTimeMillis();
            System.out.println("⚡ Schedules 병렬 로딩: " + (end - start) + "ms");
            return result;
        });
        
        CompletableFuture<DashboardSummaryResponse> summaryFuture = CompletableFuture.supplyAsync(() -> {
            long start = System.currentTimeMillis();
            DashboardSummaryResponse result = getSummary();
            long end = System.currentTimeMillis();
            System.out.println("⚡ Summary 병렬 로딩: " + (end - start) + "ms");
            return result;
        });

        try {
            // 모든 비동기 작업이 완료될 때까지 대기
            List<ItemResponse> items = itemsFuture.get();
            List<UserResponse> users = usersFuture.get();
            List<InOutOrderResponse> orders = ordersFuture.get();
            List<InventoryResponse> inventory = inventoryFuture.get();
            List<ScheduleResponse> schedules = schedulesFuture.get();
            DashboardSummaryResponse summary = summaryFuture.get();
            
            long totalEndTime = System.currentTimeMillis();
            long totalTime = totalEndTime - totalStartTime;
            
            System.out.println("🔥🔥🔥 전체 병렬 처리 완료: " + totalTime + "ms");
            
            return DashboardDataResponse.builder()
                    .items(items)
                    .users(users)
                    .orders(orders)
                    .inventory(inventory)
                    .schedules(schedules)
                    .summary(summary)
                    .totalLoadTime(totalTime)
                    .build();
                    
        } catch (Exception e) {
            System.err.println("병렬 처리 중 오류 발생: " + e.getMessage());
            throw new RuntimeException("대시보드 데이터 로딩 실패", e);
        }
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

        // 라인 차트용 시간별 데이터 생성 (예시: 최근 7일)
        List<DashboardSummaryResponse.ChartData> chartData = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            int dailyInbound = orderRepository.countByTypeAndExpectedDate(OrderType.INBOUND, date);
            int dailyOutbound = orderRepository.countByTypeAndExpectedDate(OrderType.OUTBOUND, date);
            
            chartData.add(DashboardSummaryResponse.ChartData.builder()
                    .name(date.getMonthValue() + "/" + date.getDayOfMonth())
                    .inbound(dailyInbound)
                    .outbound(dailyOutbound)
                    .value(dailyInbound + dailyOutbound) // 호환성을 위해 유지
                    .build());
        }

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
