package com.vbank.user_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegisterResponse {
    private UUID userId;
    private String username;
    private String message;
}
