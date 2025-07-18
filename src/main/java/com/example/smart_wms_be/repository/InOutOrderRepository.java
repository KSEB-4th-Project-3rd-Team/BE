package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.InOutOrder;
import com.example.smart_wms_be.domain.OrderStatus;
import com.example.smart_wms_be.domain.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InOutOrderRepository extends JpaRepository<InOutOrder, Long> {
    // 타입 + 상태로 주문 목록 조회
    List<InOutOrder> findByTypeAndStatus(OrderType type, OrderStatus status);
}
