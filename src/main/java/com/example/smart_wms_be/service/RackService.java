package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Item;
import com.example.smart_wms_be.domain.Rack;
import com.example.smart_wms_be.domain.RackInventory;
import com.example.smart_wms_be.dto.RackResponse;
import com.example.smart_wms_be.dto.RackInventoryResponse;
import com.example.smart_wms_be.dto.RackInventoryRequest;
import com.example.smart_wms_be.dto.RackMapResponse;
import com.example.smart_wms_be.repository.ItemRepository;
import com.example.smart_wms_be.repository.RackRepository;
import com.example.smart_wms_be.repository.RackInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RackService {

    private final RackRepository rackRepository;
    private final RackInventoryRepository rackInventoryRepository;
    private final ItemRepository itemRepository;

    public List<RackResponse> getAllRacks() {
        return rackRepository.findAllActiveRacks().stream()
                .map(this::convertToRackResponse)
                .collect(Collectors.toList());
    }

    /**
     * 창고맵을 위한 최적화된 랙 정보 조회
     * 위치와 활성화 상태만 반환하여 빠른 로딩 제공
     */
    public List<RackMapResponse> getRacksForMap() {
        return rackRepository.findRacksForMap().stream()
                .map(this::convertToRackMapResponse)
                .collect(Collectors.toList());
    }

    public Optional<RackResponse> getRackByCode(String rackCode) {
        return rackRepository.findByRackCode(rackCode)
                .map(this::convertToRackResponse);
    }

    public List<RackResponse> getRacksBySection(String section) {
        return rackRepository.findBySection(section).stream()
                .map(this::convertToRackResponse)
                .collect(Collectors.toList());
    }

    public List<RackInventoryResponse> getRackInventory(String rackCode) {
        return rackInventoryRepository.findByRackCode(rackCode).stream()
                .map(this::convertToRackInventoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public RackInventoryResponse updateRackInventory(String rackCode, RackInventoryRequest request) {
        Rack rack = rackRepository.findByRackCode(rackCode)
                .orElseThrow(() -> new RuntimeException("렉을 찾을 수 없습니다: " + rackCode));
        
        Item item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("아이템을 찾을 수 없습니다: " + request.getItemId()));

        Optional<RackInventory> existingInventory = rackInventoryRepository
                .findByRackIdAndItemId(rack.getId(), item.getId());

        RackInventory rackInventory;
        if (existingInventory.isPresent()) {
            rackInventory = existingInventory.get();
            rackInventory.setQuantity(request.getQuantity());
        } else {
            rackInventory = RackInventory.builder()
                    .rack(rack)
                    .item(item)
                    .quantity(request.getQuantity())
                    .build();
        }

        rackInventory = rackInventoryRepository.save(rackInventory);
        return convertToRackInventoryResponse(rackInventory);
    }

    public List<RackInventoryResponse> getAllRackInventories(String section, Boolean hasStock) {
        List<RackInventory> inventories;
        
        if (section != null) {
            inventories = rackInventoryRepository.findBySection(section);
        } else if (hasStock != null && hasStock) {
            inventories = rackInventoryRepository.findAllWithStock();
        } else {
            inventories = rackInventoryRepository.findAll();
        }

        return inventories.stream()
                .map(this::convertToRackInventoryResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void initializeRacks() {
        if (rackRepository.count() > 0) {
            return;
        }

        for (char section = 'A'; section <= 'T'; section++) {
            for (int position = 1; position <= 12; position++) {
                String rackCode = String.format("%c%03d", section, position);
                
                Rack rack = Rack.builder()
                        .rackCode(rackCode)
                        .section(String.valueOf(section))
                        .position(position)
                        .description(section + "열 " + position + "번 렉")
                        .isActive(true)
                        .build();
                
                rackRepository.save(rack);
            }
        }
    }

    public long getTotalRackCount() {
        return rackRepository.count();
    }

    public long getRacksWithInventoryCount() {
        return rackInventoryRepository.findAllWithStock().stream()
                .map(ri -> ri.getRack().getId())
                .distinct()
                .count();
    }

    private RackResponse convertToRackResponse(Rack rack) {
        List<RackInventoryResponse> inventories = rackInventoryRepository
                .findByRackId(rack.getId()).stream()
                .map(this::convertToRackInventoryResponse)
                .collect(Collectors.toList());

        return RackResponse.builder()
                .id(rack.getId())
                .rackCode(rack.getRackCode())
                .section(rack.getSection())
                .position(rack.getPosition())
                .description(rack.getDescription())
                .isActive(rack.getIsActive())
                .createdAt(rack.getCreatedAt())
                .updatedAt(rack.getUpdatedAt())
                .inventories(inventories)
                .build();
    }

    private RackInventoryResponse convertToRackInventoryResponse(RackInventory rackInventory) {
        return RackInventoryResponse.builder()
                .id(rackInventory.getId())
                .rackCode(rackInventory.getRack().getRackCode())
                .itemId(rackInventory.getItem().getId())
                .itemCode(rackInventory.getItem().getItemCode())
                .itemName(rackInventory.getItem().getItemName())
                .quantity(rackInventory.getQuantity())
                .lastUpdated(rackInventory.getLastUpdated())
                .build();
    }

    /**
     * Object[] 쿼리 결과를 RackMapResponse로 변환
     * 인덱스: 0=id, 1=rackCode, 2=section, 3=position, 4=isActive, 5=hasInventory
     */
    private RackMapResponse convertToRackMapResponse(Object[] result) {
        return RackMapResponse.builder()
                .id((Long) result[0])
                .rackCode((String) result[1])
                .section((String) result[2])
                .position((Integer) result[3])
                .isActive((Boolean) result[4])
                .hasInventory((Boolean) result[5])
                .build();
    }
}