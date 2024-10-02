package com.is4tech.base.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "winery")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Winery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "winery_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "status", nullable = false)
    private Boolean status;

}
