package com.example.smart_wms_be.controller;

import com.example.smart_wms_be.dto.*;
import com.example.smart_wms_be.service.CompanyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "거래처 관리 API")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public List<CompanyResponse> getCompanies(@RequestParam(required = false) String type) {
        return companyService.getCompanies(type);
    }

    @PostMapping
    public CompanyResponse createCompany(@RequestBody CreateCompanyRequest request) {
        return companyService.createCompany(request);
    }

    @PutMapping("/{companyId}")
    public CompanyResponse updateCompany(
            @PathVariable Long companyId,
            @RequestBody UpdateCompanyRequest request
    ) {
        return companyService.updateCompany(companyId, request);
    }

    @DeleteMapping("/{companyId}")
    public MessageResponse deleteCompany(@PathVariable Long companyId) {
        companyService.deleteCompany(companyId);
        return new MessageResponse("Company deleted successfully");
    }
}
