package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.service.RackMigrationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rack-migration")
@RequiredArgsConstructor
@Tag(name = "RackMigration", description = "렉 마이그레이션 API")
public class RackMigrationController {

    private final RackMigrationService rackMigrationService;

    @PostMapping("/migrate")
    public ResponseEntity<String> migrateInventoryToRack() {
        try {
            rackMigrationService.migrateInventoryToRack();
            return ResponseEntity.ok("기존 재고 데이터를 렉 시스템으로 성공적으로 마이그레이션했습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("마이그레이션 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}