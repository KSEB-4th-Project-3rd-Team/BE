package com.example.smart_wms_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartWmsBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartWmsBeApplication.class, args);
    }

}
