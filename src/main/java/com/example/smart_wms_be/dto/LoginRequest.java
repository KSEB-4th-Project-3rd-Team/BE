package com.example.smart_wms_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String username;
    private String password;
}
