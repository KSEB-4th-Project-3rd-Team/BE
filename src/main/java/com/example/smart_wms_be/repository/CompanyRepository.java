package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.type")
    List<Company> findAll();
    
    @Query("SELECT DISTINCT c FROM Company c LEFT JOIN FETCH c.type WHERE :type MEMBER OF c.type")
    List<Company> findByTypeContaining(@Param("type") String type);
}
