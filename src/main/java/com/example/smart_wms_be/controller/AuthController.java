package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.domain.User;
import com.example.smart_wms_be.dto.LoginRequest;
import com.example.smart_wms_be.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody LoginRequest request, HttpSession session) {
        User user = authService.login(request, session);
        return Map.of(
                "message", "Login successful",
                "user", Map.of(
                        "user_id", user.getUserId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "full_name", user.getFullName(),
                        "role", user.getRole()
                )
        );
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        authService.logout(session);
        return Map.of("message", "Logout successful");
    }

    @GetMapping("/me")
    public Map<String, Object> me(HttpSession session) {
        User user = authService.getLoginUser(session);
        return Map.of(
                "user_id", user.getUserId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "full_name", user.getFullName(),
                "role", user.getRole()
        );
    }
}
