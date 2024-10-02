package com.is4tech.base.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WineryDto {

    private String name;
    private String address;
    private String phone;
    private Integer maxCapacity;
    private Integer availableQuantity;
    private Integer userId;
    private Boolean status;
}
