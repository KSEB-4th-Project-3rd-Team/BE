package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 재고 필터링 조회 (itemCode와 locationCode 부분 일치)
    List<Inventory> findByItem_ItemCodeContainingAndLocationCodeContaining(String itemCode, String locationCode);

    // 총 재고 가치 계산
    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(i.quantity * it.unitPriceIn), 0) FROM Inventory i JOIN i.item it")
    double calculateTotalInventoryValue();
}
