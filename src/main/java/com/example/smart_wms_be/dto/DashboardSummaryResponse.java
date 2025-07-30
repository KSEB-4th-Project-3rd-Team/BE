package com.example.smart_wms_be.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 대시보드 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private InventorySummary inventorySummary;
    private WorkStatusSummary workStatusSummary;
    private InOutAnalysis inOutAnalysis;
    private AmrAnalysis amrAnalysis;
    private SalesAnalysis salesAnalysis;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InventorySummary {
        private int totalItems;
        private int normalStockItems;
        private int lowStockItems;
        private int outOfStockItems;
        private int totalQuantity;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkStatusSummary {
        private int completedToday;
        private int inProgressToday;
        private int pendingToday;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InOutAnalysis {
        private int totalInbound;
        private int totalOutbound;
        private double completionRate;
        private List<ChartData> chartData;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AmrAnalysis {
        private int totalAmrs;
        private int activeAmrs;
        private int errorAmrs;
        private List<ChartData> statusDistribution;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesAnalysis {
        private double totalSalesAmount;
        private int totalSalesCount;
        private List<ChartData> companySalesDistribution;
        private List<ChartData> salesTrend;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private String name;
        private int value;
        private Integer inbound;
        private Integer outbound;
    }
}
