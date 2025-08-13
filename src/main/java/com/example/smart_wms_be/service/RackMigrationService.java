package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Inventory;
import com.example.smart_wms_be.domain.Rack;
import com.example.smart_wms_be.domain.RackInventory;
import com.example.smart_wms_be.repository.InventoryRepository;
import com.example.smart_wms_be.repository.RackRepository;
import com.example.smart_wms_be.repository.RackInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RackMigrationService {

    private final InventoryRepository inventoryRepository;
    private final RackRepository rackRepository;
    private final RackInventoryRepository rackInventoryRepository;

    @Transactional
    public void migrateInventoryToRack() {
        List<Inventory> inventories = inventoryRepository.findAll();
        int migratedCount = 0;
        int skippedCount = 0;

        log.info("기존 재고 데이터 {} 건을 렉 시스템으로 마이그레이션 시작", inventories.size());

        for (int i = 0; i < inventories.size(); i++) {
            Inventory inventory = inventories.get(i);
            String rackCode = convertLocationCodeToRackCode(inventory.getLocationCode());
            
            // locationCode가 null이거나 변환할 수 없으면 순차적으로 렉에 할당
            if (rackCode == null) {
                // A001~A012, B001~B012, ... 순서로 분산 배치
                char section = (char)('A' + (i % 20)); // A~T (20개 섹션)
                int position = (i % 12) + 1; // 1~12
                rackCode = String.format("%c%03d", section, position);
                log.info("LocationCode {} 를 렉 코드로 변환할 수 없어 렉 {}에 할당합니다.", inventory.getLocationCode(), rackCode);
            }

            Optional<Rack> rackOpt = rackRepository.findByRackCode(rackCode);
            if (rackOpt.isEmpty()) {
                log.warn("렉 코드 {} 에 해당하는 렉을 찾을 수 없습니다. 건너뜁니다.", rackCode);
                skippedCount++;
                continue;
            }

            Rack rack = rackOpt.get();
            
            Optional<RackInventory> existingRackInventory = rackInventoryRepository
                    .findByRackIdAndItemId(rack.getId(), inventory.getItem().getId());

            if (existingRackInventory.isPresent()) {
                RackInventory rackInventory = existingRackInventory.get();
                rackInventory.setQuantity(inventory.getQuantity());
                rackInventoryRepository.save(rackInventory);
                log.debug("기존 렉 재고 업데이트: {} - {} (수량: {})", rackCode, inventory.getItem().getItemCode(), inventory.getQuantity());
            } else {
                RackInventory newRackInventory = RackInventory.builder()
                        .rack(rack)
                        .item(inventory.getItem())
                        .quantity(inventory.getQuantity())
                        .build();
                rackInventoryRepository.save(newRackInventory);
                log.debug("새 렉 재고 생성: {} - {} (수량: {})", rackCode, inventory.getItem().getItemCode(), inventory.getQuantity());
            }
            
            migratedCount++;
        }

        log.info("렉 마이그레이션 완료: {} 건 마이그레이션, {} 건 건너뜀", migratedCount, skippedCount);
    }

    private String convertLocationCodeToRackCode(String locationCode) {
        if (locationCode == null || locationCode.trim().isEmpty()) {
            return null; // null이면 호출부에서 기본값 처리
        }

        // A-01 형태를 A001 형태로 변환
        String[] parts = locationCode.split("-");
        if (parts.length != 2) {
            return null;
        }

        try {
            String section = parts[0].trim().toUpperCase();
            int position = Integer.parseInt(parts[1].trim());
            
            // A~T 범위 체크
            if (section.length() != 1 || section.charAt(0) < 'A' || section.charAt(0) > 'T') {
                return null;
            }
            
            // 1~12 범위 체크
            if (position < 1 || position > 12) {
                return null;
            }
            
            return String.format("%s%03d", section, position);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Transactional
    public void syncInventoryToRack(Inventory inventory) {
        String rackCode = convertLocationCodeToRackCode(inventory.getLocationCode());
        
        if (rackCode == null) {
            log.debug("LocationCode {} 를 렉 코드로 변환할 수 없습니다.", inventory.getLocationCode());
            return;
        }

        Optional<Rack> rackOpt = rackRepository.findByRackCode(rackCode);
        if (rackOpt.isEmpty()) {
            log.debug("렉 코드 {} 에 해당하는 렉을 찾을 수 없습니다.", rackCode);
            return;
        }

        Rack rack = rackOpt.get();
        
        Optional<RackInventory> existingRackInventory = rackInventoryRepository
                .findByRackIdAndItemId(rack.getId(), inventory.getItem().getId());

        if (existingRackInventory.isPresent()) {
            RackInventory rackInventory = existingRackInventory.get();
            rackInventory.setQuantity(inventory.getQuantity());
            rackInventoryRepository.save(rackInventory);
        } else {
            RackInventory newRackInventory = RackInventory.builder()
                    .rack(rack)
                    .item(inventory.getItem())
                    .quantity(inventory.getQuantity())
                    .build();
            rackInventoryRepository.save(newRackInventory);
        }
        
        log.debug("재고 변경 시 렉 재고 동기화: {} - {} (수량: {})", 
                rackCode, inventory.getItem().getItemCode(), inventory.getQuantity());
    }
}