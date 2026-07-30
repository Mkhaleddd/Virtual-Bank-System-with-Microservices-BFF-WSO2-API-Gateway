package com.vbank.logging_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogMessageDto {

    @JsonProperty("serviceName")
    private String serviceName;   

    @JsonProperty("eventType")
    private String eventType;  

    @JsonProperty("accountId")
    private String accountId;

    @JsonProperty("message")
    private String message;       

    @JsonProperty("messageType")
    private String messageType;  

    @JsonProperty("dateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant dateTime;    
}