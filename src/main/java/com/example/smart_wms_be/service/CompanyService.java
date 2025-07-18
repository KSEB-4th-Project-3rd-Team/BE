package com.example.smart_wms_be.service;

import com.example.smart_wms_be.domain.Company;
import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompanyResponse> getCompanies(String type) {
        List<Company> companies = (type == null)
                ? companyRepository.findAll()
                : companyRepository.findByTypeContaining(type);

        return companies.stream()
                .map(CompanyResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CompanyResponse createCompany(CreateCompanyRequest request) {
        Company company = Company.builder()
                .companyName(request.getCompanyName())
                .companyCode(request.getCompanyCode())
                .address(request.getAddress())
                .contactPerson(request.getContactPerson())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .type(request.getType())
                .build();
        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    public CompanyResponse updateCompany(Long id, UpdateCompanyRequest request) {
        Company company = companyRepository.findById(id).orElseThrow();
        company.setAddress(request.getAddress());
        company.setContactPerson(request.getContactPerson());
        return CompanyResponse.fromEntity(companyRepository.save(company));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
