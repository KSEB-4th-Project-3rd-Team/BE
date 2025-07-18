package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // 별도 메서드 필요 없음 (기본 CRUD 사용)
}
