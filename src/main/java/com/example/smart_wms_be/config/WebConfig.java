package com.example.smart_wms_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API에 대해
                .allowedOrigins("http://localhost:3000") // FE 주소
                .allowedMethods("*") // GET, POST, PUT 등 허용
                .allowedHeaders("*")
                .allowCredentials(true); // 세션 등 인증 허용 시 필요
    }
}
