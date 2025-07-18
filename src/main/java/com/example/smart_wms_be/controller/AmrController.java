package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.AmrResponse;
import com.example.smart_wms_be.service.AmrService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AMR 상태 API 컨트롤러
 */
@RestController
@RequestMapping("/api/amrs")
@RequiredArgsConstructor
@Tag(name = "AMR", description = "AMR 상태 체크 API")
public class AmrController {

    private final AmrService amrService;

    // 전체 AMR 상태 조회
    @GetMapping
    public List<AmrResponse> getAmrStatuses() {
        return amrService.getAllAmrs();
    }
}
