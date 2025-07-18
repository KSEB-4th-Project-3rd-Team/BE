package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.ScheduleType;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 스케줄 생성 요청 DTO
 */
@Getter
public class CreateScheduleRequest {

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ScheduleType type;
}
