package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 스케줄 조회 응답 DTO
 */
@Getter
@Builder
public class ScheduleResponse {

    private Long scheduleId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String type;

    public static ScheduleResponse fromEntity(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getId())
                .title(schedule.getTitle())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .type(schedule.getType().name())
                .build();
    }
}
