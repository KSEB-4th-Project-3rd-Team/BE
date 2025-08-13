package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
    
    Optional<Rack> findByRackCode(String rackCode);
    
    List<Rack> findBySection(String section);
    
    List<Rack> findByPosition(Integer position);
    
    List<Rack> findBySectionAndPosition(String section, Integer position);
    
    @Query("SELECT r FROM Rack r WHERE r.isActive = true ORDER BY r.section, r.position")
    List<Rack> findAllActiveRacks();
    
    @Query("SELECT r FROM Rack r JOIN FETCH r.rackInventories ri WHERE r.rackCode = :rackCode")
    Optional<Rack> findByRackCodeWithInventories(@Param("rackCode") String rackCode);
}