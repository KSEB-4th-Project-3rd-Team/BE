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
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date
    ) {
        return scheduleService.getSchedules(start_date, end_date);
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
