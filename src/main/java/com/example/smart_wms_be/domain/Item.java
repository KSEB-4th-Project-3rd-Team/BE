package com.example.smart_wms_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemCode;       // 품목 코드 (SKU)
    private String itemName;       // 품목명
    private String itemGroup;      // 품목 그룹
    private String spec;           // 규격
    private String unit;           // 단위

    private Double unitPriceIn;    // 입고 단가
    private Double unitPriceOut;   // 출고 단가
}
