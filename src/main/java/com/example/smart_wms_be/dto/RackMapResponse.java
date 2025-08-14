package com.example.smart_wms_be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 창고맵을 위한 경량화된 Rack 응답 DTO
 * 위치 정보와 활성화 상태만 포함하여 빠른 로딩 제공
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RackMapResponse {
    private Long id;
    private String rackCode;
    private String section;
    private Integer position;
    private Boolean isActive;
    private Boolean hasInventory; // 재고 유무만 표시 (개수는 제외)
}