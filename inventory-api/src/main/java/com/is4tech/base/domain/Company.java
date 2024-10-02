package com.is4tech.base.domain;


import jakarta.persistence.*;
import lombok.*;

@Table(name="company")
@Entity
@Setter 
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "description", nullable = false,length = 255)
    private String description;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "status", nullable = false)
    private Boolean status;
}
