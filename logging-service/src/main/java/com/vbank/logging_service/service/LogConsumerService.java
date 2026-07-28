package com.vbank.logging_service.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vbank.logging_service.dto.LogMessageDto;
import com.vbank.logging_service.model.LogDump;
import com.vbank.logging_service.repository.LogRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class LogConsumerService {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

  
    public LogConsumerService(LogRepository logRepository, ObjectMapper objectMapper) {
        this.logRepository = logRepository;
        this.objectMapper = objectMapper.copy()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @KafkaListener(topics = "logging-topic", groupId = "logging-group")
    public void listen(String messagePayload) {
        if (messagePayload == null || messagePayload.isBlank()) {
            log.warn("Received empty or null message payload from Kafka");
            return;
        }

        try {
            LogMessageDto dto = objectMapper.readValue(messagePayload, LogMessageDto.class);

            LogDump logDump = new LogDump();
            logDump.setAccountId(dto.getAccountId());
            logDump.setMessageType(dto.getMessageType());
            logDump.setMessage(dto.getMessage());
            logDump.setDateTime(dto.getDateTime() != null ? dto.getDateTime() : Instant.now());
            logDump.setEventType(dto.getEventType());
            logRepository.save(logDump);
            log.info("Saved log entry of type: {} for account ID: {}", dto.getMessageType(), dto.getAccountId());

        } catch (Exception e) {
            log.error("Failed to process incoming log message: {}", messagePayload, e);
        }
    }
}