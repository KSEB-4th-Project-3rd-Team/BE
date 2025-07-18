package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Notification;
import com.example.smart_wms_be.dto.MessageResponse;
import com.example.smart_wms_be.dto.NotificationResponse;
import com.example.smart_wms_be.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getAll() {
        return notificationRepository.findAll().stream()
                .map(NotificationResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public MessageResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("알림 없음"));

        notification = Notification.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(true)
                .createdAt(notification.getCreatedAt())
                .build();

        notificationRepository.save(notification);

        return new MessageResponse("Notification marked as read");
    }
}
