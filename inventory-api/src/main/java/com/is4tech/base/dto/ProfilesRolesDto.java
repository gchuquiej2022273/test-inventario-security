package com.is4tech.base.dto;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProfilesRolesDto implements Serializable {
    private Integer profileId;
    private Integer roleId;
}