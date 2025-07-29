package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Company;
import com.example.smart_wms_be.domain.InOutOrder;
import com.example.smart_wms_be.domain.OrderStatus;
import com.example.smart_wms_be.domain.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface InOutOrderRepository extends JpaRepository<InOutOrder, Long> {

    // 주문 유형 + 상태로 조회
    List<InOutOrder> findByTypeAndStatus(OrderType type, OrderStatus status);

    // 주문 유형 + 상태로 개수 조회
    int countByTypeAndStatus(OrderType type, OrderStatus status);

    // 상태 + 날짜로 개수 조회 (대시보드용)
    int countByStatusAndExpectedDate(OrderStatus status, LocalDate expectedDate);

    // 주문 유형별 개수 조회 (대시보드용)
    int countByType(OrderType type);

    // 거래처별 주문 개수 조회 (대시보드용)
    int countByCompany(Company company);
}
