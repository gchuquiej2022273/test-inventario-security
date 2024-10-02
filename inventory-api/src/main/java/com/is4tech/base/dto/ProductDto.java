package com.is4tech.base.dto;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;
    private Integer deliveryTime;
    private String barCode;
    private String description;
    private Integer availableQuantity;
    private BigDecimal price;
    private Boolean status;
    private Integer companyId;
    private Integer categoryId;
}
