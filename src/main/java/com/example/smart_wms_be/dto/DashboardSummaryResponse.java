package com.example.smart_wms_be.dto;

import lombok.*;

import java.util.Map;

/**
 * 대시보드 응답 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private double totalInventoryValue;
    private int inboundPending;
    private int outboundPending;
    private Map<String, Integer> amrStatus;
}
