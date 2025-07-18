package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.User;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 전체 사용자 조회
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 사용자 생성
    public UserResponse createUser(CreateUserRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .role(request.getRole())
                .isActive(true)
                .build();
        return UserResponse.fromEntity(userRepository.save(user));
    }

    // 사용자 수정
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // ⬇ setter 대신 커스텀 메서드 사용
        user.updateUserInfo(
                request.getFullName(),
                request.getEmail(),
                request.getRole(),
                request.getIsActive()
        );

        return UserResponse.fromEntity(userRepository.save(user));
    }

    // 사용자 삭제
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
