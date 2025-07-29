package com.example.smart_wms_be.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class UpdateCompanyRequest {
    private String companyCode;
    private String companyName;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private List<String> type;
}
