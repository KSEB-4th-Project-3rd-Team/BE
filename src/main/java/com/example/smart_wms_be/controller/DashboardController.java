package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.DashboardDataResponse;
import com.example.smart_wms_be.dto.DashboardSummaryResponse;
import com.example.smart_wms_be.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 대시보드 요약 API 컨트롤러
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard", description = "대시보드 요약 정보 API")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardService.getSummary();
    }

    // 🚀 새로운 통합 API - 모든 대시보드 데이터를 병렬로 가져옴
    @GetMapping("/all")
    public DashboardDataResponse getAllDashboardData() {
        long startTime = System.currentTimeMillis();
        DashboardDataResponse result = dashboardService.getAllDashboardData();
        long endTime = System.currentTimeMillis();
        System.out.println("🚀🚀🚀 TOTAL DASHBOARD API 실행시간: " + (endTime - startTime) + "ms");
        return result;
    }
}
