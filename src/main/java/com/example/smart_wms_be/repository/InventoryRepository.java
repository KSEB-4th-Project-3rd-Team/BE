package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Inventory;
import com.example.smart_wms_be.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // 모든 재고를 최신 업데이트순으로 조회 (ID 역순) - N+1 문제 해결
    @org.springframework.data.jpa.repository.Query("SELECT i FROM Inventory i " +
                                                   "LEFT JOIN FETCH i.item " +
                                                   "ORDER BY i.id DESC")
    List<Inventory> findAllByOrderByIdDesc();

    // 재고 필터링 조회 (itemCode와 locationCode 부분 일치) - N+1 해결
    @org.springframework.data.jpa.repository.Query("SELECT i FROM Inventory i " +
                                                   "LEFT JOIN FETCH i.item " +
                                                   "WHERE i.item.itemCode LIKE %:itemCode% AND i.locationCode LIKE %:locationCode%")
    List<Inventory> findByItem_ItemCodeContainingAndLocationCodeContaining(
        @org.springframework.data.repository.query.Param("itemCode") String itemCode, 
        @org.springframework.data.repository.query.Param("locationCode") String locationCode);

    // 특정 아이템과 위치로 재고 찾기
    Optional<Inventory> findByItemAndLocationCode(Item item, String locationCode);
    
    // 특정 아이템으로 재고 찾기 (DB 제약조건 ux_inventory_item 대응)
    Optional<Inventory> findByItem(Item item);

    // 총 재고 가치 계산
    @org.springframework.data.jpa.repository.Query("SELECT COALESCE(SUM(i.quantity * it.unitPriceIn), 0) FROM Inventory i JOIN i.item it")
    double calculateTotalInventoryValue();
}
