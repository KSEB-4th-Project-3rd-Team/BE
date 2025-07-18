package com.example.smart_wms_be.dto;

import com.example.smart_wms_be.domain.Company;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CompanyResponse {
    private Long companyId;
    private String companyName;
    private String companyCode;
    private String address;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private List<String> type;

    public static CompanyResponse fromEntity(Company company) {
        return CompanyResponse.builder()
                .companyId(company.getId())
                .companyName(company.getCompanyName())
                .companyCode(company.getCompanyCode())
                .address(company.getAddress())
                .contactPerson(company.getContactPerson())
                .contactEmail(company.getContactEmail())
                .contactPhone(company.getContactPhone())
                .type(company.getType())
                .build();
    }
}
