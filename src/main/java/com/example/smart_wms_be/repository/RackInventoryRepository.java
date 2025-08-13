package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.RackInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RackInventoryRepository extends JpaRepository<RackInventory, Long> {
    
    List<RackInventory> findByRackId(Long rackId);
    
    List<RackInventory> findByItemId(Long itemId);
    
    Optional<RackInventory> findByRackIdAndItemId(Long rackId, Long itemId);
    
    @Query("SELECT ri FROM RackInventory ri JOIN FETCH ri.rack JOIN FETCH ri.item WHERE ri.rack.rackCode = :rackCode")
    List<RackInventory> findByRackCode(@Param("rackCode") String rackCode);
    
    @Query("SELECT ri FROM RackInventory ri JOIN FETCH ri.rack JOIN FETCH ri.item WHERE ri.quantity > 0")
    List<RackInventory> findAllWithStock();
    
    @Query("SELECT ri FROM RackInventory ri JOIN FETCH ri.rack JOIN FETCH ri.item WHERE ri.rack.section = :section")
    List<RackInventory> findBySection(@Param("section") String section);
}