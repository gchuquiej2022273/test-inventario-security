package com.is4tech.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
