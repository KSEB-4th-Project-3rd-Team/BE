package com.example.smart_wms_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 프론트/개발 도메인
                .allowedOriginPatterns(
                        "http://localhost:3000",
                        "http://localhost:8081",
                        "https://localhost:8081",
                        "https://smart-wms-fe.vercel.app",
                        "https://*.vercel.app" // ← 브랜치 프리뷰까지 허용하려면 유지, 아니면 이 줄 지워도 됨
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // 프리플라이트 캐시(초)
    }
}
