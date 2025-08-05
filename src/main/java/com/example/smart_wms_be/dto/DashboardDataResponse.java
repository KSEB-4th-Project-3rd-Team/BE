package com.example.smart_wms_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataResponse {
    
    private List<ItemResponse> items;
    private List<UserResponse> users;
    private List<InOutOrderResponse> orders;
    private List<InventoryResponse> inventory;
    private List<ScheduleResponse> schedules;
    private DashboardSummaryResponse summary;
    
    // 각 데이터 로딩 시간 (디버깅용)
    private Long itemsLoadTime;
    private Long usersLoadTime;
    private Long ordersLoadTime;
    private Long inventoryLoadTime;
    private Long schedulesLoadTime;
    private Long totalLoadTime;
}