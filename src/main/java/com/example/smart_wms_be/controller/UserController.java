package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User 관리", description = "User 관리 관련 API")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        long startTime = System.currentTimeMillis();
        List<UserResponse> result = userService.getAllUsers();
        long endTime = System.currentTimeMillis();
        System.out.println("🚀 UserController.getAllUsers() 실행시간: " + (endTime - startTime) + "ms, 결과 개수: " + result.size());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().body("{\"message\": \"User deleted successfully\"}");
    }
}
