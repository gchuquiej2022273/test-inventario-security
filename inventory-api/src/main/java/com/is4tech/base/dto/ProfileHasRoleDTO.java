package com.is4tech.base.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileHasRoleDTO {
    private Integer profileId;
    private String profileName;
    private Integer roleId;
    private String roleName;

}
