package com.vbank.user_service.user.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "password is required")
    private String password;
}
