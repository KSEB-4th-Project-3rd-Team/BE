package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Role;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private Role role;
}
