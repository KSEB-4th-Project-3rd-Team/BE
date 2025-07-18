package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.MessageResponse;
import com.example.smart_wms_be.dto.NotificationResponse;
import com.example.smart_wms_be.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 API")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getAllNotifications() {
        return notificationService.getAll();
    }

    @PostMapping("/{id}/read")
    public MessageResponse markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }
}
