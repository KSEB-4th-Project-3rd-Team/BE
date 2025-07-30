package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.User;
import com.example.smart_wms_be.domain.Status;
import com.example.smart_wms_be.dto.LoginRequest;
import com.example.smart_wms_be.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        // 로그인 시간 업데이트하고 활성 상태로 변경
        user.updateLastLogin();
        user.updateUserInfo(user.getFullName(), user.getEmail(), user.getRole(), Status.ACTIVE);
        userRepository.save(user);
        
        session.setAttribute("loginUser", user);
        return user;
    }

    public void logout(HttpSession session) {
        try {
            User user = getLoginUser(session);
            user.updateUserInfo(user.getFullName(), user.getEmail(), user.getRole(), Status.INACTIVE);
            userRepository.save(user);
        } catch (RuntimeException e) {
            // 세션에 사용자가 없어도 로그아웃은 진행
        }
        session.invalidate();
    }

    public User getLoginUser(HttpSession session) {
        Object user = session.getAttribute("loginUser");
        if (user == null) throw new RuntimeException("로그인되지 않은 사용자입니다.");
        return (User) user;
    }
}
