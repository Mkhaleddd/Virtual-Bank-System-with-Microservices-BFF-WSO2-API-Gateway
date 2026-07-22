package com.vbank.logging_service.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LogMessageDto {
    private String message;
    private String messageType;
    private Instant dateTime;
}