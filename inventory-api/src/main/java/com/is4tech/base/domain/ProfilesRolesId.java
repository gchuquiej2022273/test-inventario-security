package com.is4tech.base.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class ProfilesRolesId implements Serializable {
    private Integer profileId;
    private Integer roleId;
}