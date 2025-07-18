package com.example.smart_wms_be.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String companyCode;
    private String address;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "company_types", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "type")
    private List<String> type; // e.g., ["CLIENT", "SUPPLIER"]
}
