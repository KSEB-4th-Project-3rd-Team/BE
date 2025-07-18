package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Amr;
import com.example.smart_wms_be.domain.AmrStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AmrResponse {
    private Long amrId;
    private String amrName;
    private AmrStatus status;
    private double batteryLevel;
    private String currentLocation;

    public static AmrResponse fromEntity(Amr amr) {
        return AmrResponse.builder()
                .amrId(amr.getId())
                .amrName(amr.getName())
                .status(amr.getStatus())
                .batteryLevel(amr.getBatteryLevel())
                .currentLocation(amr.getCurrentLocation())
                .build();
    }
}
