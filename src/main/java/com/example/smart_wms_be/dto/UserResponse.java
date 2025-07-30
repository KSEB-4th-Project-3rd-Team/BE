package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Role;
import com.example.smart_wms_be.domain.Status;
import com.example.smart_wms_be.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserResponse {
    private Long userId;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private Status status;
    private LocalDateTime lastLogin;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .status(user.getStatus())
                .lastLogin(user.getLastLogin())
                .build();
    }
}
