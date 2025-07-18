package com.example.smart_wms_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv; // env 파일 로드용

@SpringBootApplication
public class SmartWmsBeApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load(); // env 파일 로드.
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        SpringApplication.run(SmartWmsBeApplication.class, args);
    }

}
