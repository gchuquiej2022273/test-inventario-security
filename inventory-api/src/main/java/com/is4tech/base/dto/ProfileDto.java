package com.is4tech.base.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String name;
    private String description;
    private Boolean status;
    private List<String> resource;
}
