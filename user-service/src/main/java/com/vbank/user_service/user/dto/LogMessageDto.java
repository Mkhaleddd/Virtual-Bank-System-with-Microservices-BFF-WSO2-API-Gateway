package com.vbank.user_service.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageDto {

    private String serviceName;   
    private String eventType;  
    private Long accountId;    
    private String message;       
    private String messageType;  
    private Instant dateTime;    
}