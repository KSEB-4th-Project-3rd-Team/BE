package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.CreateScheduleRequest;
import com.example.smart_wms_be.dto.MessageResponse;
import com.example.smart_wms_be.dto.ScheduleResponse;
import com.example.smart_wms_be.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 스케줄 API 컨트롤러
 */
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Tag(name = "Schedule", description = "달력 스케쥴 API")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public List<ScheduleResponse> getSchedules(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date
    ) {
        long startTime = System.currentTimeMillis();
        // 파라미터가 없으면 기본값 설정 (현재 달)
        if (start_date == null) {
            start_date = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        }
        if (end_date == null) {
            end_date = start_date.plusMonths(1).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        }
        List<ScheduleResponse> result = scheduleService.getSchedules(start_date, end_date);
        long endTime = System.currentTimeMillis();
        System.out.println("🚀 ScheduleController.getSchedules() 실행시간: " + (endTime - startTime) + "ms, 결과 개수: " + result.size());
        return result;
    }

    @PostMapping
    public ScheduleResponse createSchedule(@RequestBody CreateScheduleRequest request) {
        return scheduleService.createSchedule(request);
    }

    @DeleteMapping("/{scheduleId}")
    public MessageResponse deleteSchedule(@PathVariable Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);
        return new MessageResponse("Schedule deleted successfully");
    }
}
