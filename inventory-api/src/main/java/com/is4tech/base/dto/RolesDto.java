package com.is4tech.base.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {
    private String name;
    private String description;
    private Boolean status;
}
