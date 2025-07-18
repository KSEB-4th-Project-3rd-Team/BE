package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
