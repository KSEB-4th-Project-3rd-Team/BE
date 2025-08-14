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
    
    /**
     * N+1 쿼리 문제 해결을 위한 JOIN FETCH 최적화
     * 랙과 재고를 한 번에 조회하여 성능 개선
     */
    @Query("SELECT DISTINCT r FROM Rack r LEFT JOIN FETCH r.rackInventories ri LEFT JOIN FETCH ri.item " +
           "WHERE r.isActive = true ORDER BY r.section, r.position")
    List<Rack> findAllActiveRacksWithInventories();
    
    @Query("SELECT r FROM Rack r JOIN FETCH r.rackInventories ri WHERE r.rackCode = :rackCode")
    Optional<Rack> findByRackCodeWithInventories(@Param("rackCode") String rackCode);
    
    /**
     * 창고맵을 위한 최적화된 쿼리
     * 랙 정보와 재고 유무를 한 번의 쿼리로 조회
     */
    @Query("SELECT r.id, r.rackCode, r.section, r.position, r.isActive, " +
           "CASE WHEN COUNT(ri.id) > 0 THEN true ELSE false END as hasInventory " +
           "FROM Rack r LEFT JOIN r.rackInventories ri " +
           "WHERE r.isActive = true " +
           "GROUP BY r.id, r.rackCode, r.section, r.position, r.isActive " +
           "ORDER BY r.section, r.position")
    List<Object[]> findRacksForMap();
}