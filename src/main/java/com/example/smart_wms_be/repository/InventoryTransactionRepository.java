package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    // 모든 거래 내역을 최신순으로 조회 (ID 역순)
    List<InventoryTransaction> findAllByOrderByIdDesc();
}
