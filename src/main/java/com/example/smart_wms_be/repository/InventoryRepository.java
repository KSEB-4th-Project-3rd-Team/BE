package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByItem_ItemCodeContainingAndLocationCodeContaining(String itemCode, String locationCode);
}
