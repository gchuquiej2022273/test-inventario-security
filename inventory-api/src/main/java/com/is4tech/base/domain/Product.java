package com.is4tech.base.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "product")
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "delivery_time", nullable = false)
    private Integer deliveryTime;

    @Column(name = "bar_code", nullable = false, length = 60, unique = true)
    private String barCode;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "category_id")
    private Integer categoryId;

}
