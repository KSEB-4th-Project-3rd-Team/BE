package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.User;
import com.example.smart_wms_be.dto.LoginRequest;
import com.example.smart_wms_be.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public User login(LoginRequest request, HttpSession session) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        session.setAttribute("loginUser", user);
        return user;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public User getLoginUser(HttpSession session) {
        Object user = session.getAttribute("loginUser");
        if (user == null) throw new RuntimeException("로그인되지 않은 사용자입니다.");
        return (User) user;
    }
}
