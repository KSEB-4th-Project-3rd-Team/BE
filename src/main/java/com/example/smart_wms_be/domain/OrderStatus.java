package com.example.smart_wms_be.domain;

public enum OrderStatus {
    PENDING,     // 승인대기
    SCHEDULED,   // 예약 (승인됨)  
    REJECTED,    // 거절
    COMPLETED,   // 완료
    CANCELLED    // 취소
}
