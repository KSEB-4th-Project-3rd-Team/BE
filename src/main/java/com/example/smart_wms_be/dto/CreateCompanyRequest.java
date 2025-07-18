package com.example.smart_wms_be.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateCompanyRequest {
    private String companyName;
    private String companyCode;
    private String address;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;
    private List<String> type;
}
