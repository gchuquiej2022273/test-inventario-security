package com.is4tech.base.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Login {
    private String token;
    private long expiresIn;
}
