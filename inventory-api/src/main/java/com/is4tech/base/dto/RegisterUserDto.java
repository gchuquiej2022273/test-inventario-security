package com.is4tech.base.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String username;
    private Integer age;
    private String phone;
    private Integer profileId;
    private Boolean status;
}
