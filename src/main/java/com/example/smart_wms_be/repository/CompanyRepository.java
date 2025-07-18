package com.example.smart_wms_be.repository;

import com.example.smart_wms_be.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByTypeContaining(String type);
}
