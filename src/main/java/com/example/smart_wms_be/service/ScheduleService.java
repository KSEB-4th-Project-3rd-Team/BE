package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Schedule;
import com.example.smart_wms_be.dto.CreateScheduleRequest;
import com.example.smart_wms_be.dto.ScheduleResponse;
import com.example.smart_wms_be.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 스케줄 비즈니스 로직
 */
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleResponse> getSchedules(LocalDateTime start, LocalDateTime end) {
        return scheduleRepository.findByStartTimeBetween(start, end).stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public ScheduleResponse createSchedule(CreateScheduleRequest request) {
        Schedule saved = scheduleRepository.save(
                Schedule.builder()
                        .title(request.getTitle())
                        .startTime(request.getStartTime())
                        .endTime(request.getEndTime())
                        .type(request.getType())
                        .build()
        );
        return ScheduleResponse.fromEntity(saved);
    }
}
