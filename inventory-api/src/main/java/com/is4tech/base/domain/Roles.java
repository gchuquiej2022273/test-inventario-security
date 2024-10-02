package com.is4tech.base.domain;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "roles")
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roles_id", nullable = false)
    private Integer roleId;

    @Column(name = "name", nullable = false, length = 80, unique = true)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "status", nullable = false)
    private boolean status;
}
