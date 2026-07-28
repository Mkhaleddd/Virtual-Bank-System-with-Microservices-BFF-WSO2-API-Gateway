package com.vbank.logging_service.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class LogSearchCriteria {
    private String messageType; 
    private String keyword;     
    private Instant startTime;
    private Instant endTime;
}