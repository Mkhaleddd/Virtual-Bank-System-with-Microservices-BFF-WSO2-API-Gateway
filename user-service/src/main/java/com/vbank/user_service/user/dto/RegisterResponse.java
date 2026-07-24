package com.vbank.user_service.user.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RegisterResponse {
    private UUID userId;
    private String username;
    private String message;
}
