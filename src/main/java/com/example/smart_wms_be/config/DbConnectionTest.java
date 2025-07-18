package com.example.smart_wms_be.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DbConnectionTest implements CommandLineRunner {

    private final DataSource dataSource;

    public DbConnectionTest(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ DB 연결 성공: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            System.out.println("❌ DB 연결 실패: " + e.getMessage());
        }
    }
}
