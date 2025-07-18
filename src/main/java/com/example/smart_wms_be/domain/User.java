package com.example.smart_wms_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")  // 실제 DB 테이블명
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")  // DB 필드와 매칭
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // 로그인 시 마지막 로그인 시간 업데이트
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    // 비밀번호 변경 등 필요 시 추가 메서드도 가능
    public void updateUserInfo(String fullName, String email, Role role, Boolean isActive) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }
}
