package com.is4tech.base.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
    private String name;
    private String description;
    private String phone;
    private String address;
    private Boolean status;
}
