package com.example.smart_wms_be.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int requestedQuantity;
    private int processedQuantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private InOutOrder order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
