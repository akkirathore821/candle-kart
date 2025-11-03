package com.candlekart.inventory_service.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long productId;

    @Column(unique = true, nullable = false)
    private String skuCode;

    @Column(nullable = false)
    private Integer quantity;
}