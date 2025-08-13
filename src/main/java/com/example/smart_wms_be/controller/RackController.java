package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.RackResponse;
import com.example.smart_wms_be.dto.RackInventoryResponse;
import com.example.smart_wms_be.dto.RackInventoryRequest;
import com.example.smart_wms_be.service.RackService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/racks")
@RequiredArgsConstructor
@Tag(name = "Rack", description = "렉 관리 API")
public class RackController {

    private final RackService rackService;

    @GetMapping
    public List<RackResponse> getAllRacks() {
        return rackService.getAllRacks();
    }

    @GetMapping("/{rackCode}")
    public ResponseEntity<RackResponse> getRackByCode(@PathVariable String rackCode) {
        return rackService.getRackByCode(rackCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/section/{section}")
    public List<RackResponse> getRacksBySection(@PathVariable String section) {
        return rackService.getRacksBySection(section);
    }

    @GetMapping("/{rackCode}/inventory")
    public List<RackInventoryResponse> getRackInventory(@PathVariable String rackCode) {
        return rackService.getRackInventory(rackCode);
    }

    @PostMapping("/{rackCode}/inventory")
    public ResponseEntity<RackInventoryResponse> updateRackInventory(
            @PathVariable String rackCode,
            @RequestBody RackInventoryRequest request) {
        try {
            RackInventoryResponse response = rackService.updateRackInventory(rackCode, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/inventory")
    public List<RackInventoryResponse> getAllRackInventories(
            @RequestParam(required = false) String section,
            @RequestParam(required = false) Boolean hasStock) {
        return rackService.getAllRackInventories(section, hasStock);
    }

    @PostMapping("/initialize")
    public ResponseEntity<String> initializeRacks() {
        rackService.initializeRacks();
        return ResponseEntity.ok("240개 렉이 성공적으로 초기화되었습니다.");
    }

    @GetMapping("/inventory/summary")
    public ResponseEntity<String> getInventorySummary() {
        long totalRacks = rackService.getTotalRackCount();
        long racksWithInventory = rackService.getRacksWithInventoryCount();
        long emptyRacks = totalRacks - racksWithInventory;
        
        String summary = String.format(
            "렉 현황: 총 %d개 렉 중 %d개 렉에 재고 있음, %d개 렉 비어있음", 
            totalRacks, racksWithInventory, emptyRacks
        );
        
        return ResponseEntity.ok(summary);
    }
}