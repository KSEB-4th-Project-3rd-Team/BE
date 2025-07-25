package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Role;
import lombok.Getter;

@Getter
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private Role role;
    private Boolean isActive;
}
