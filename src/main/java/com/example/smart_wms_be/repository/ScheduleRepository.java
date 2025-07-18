package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 스케줄 Repository
 */
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
}
