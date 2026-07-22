package com.vbank.logging_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vbank.logging_service.dto.LogMessageDto;
import com.vbank.logging_service.model.LogDump;
import com.vbank.logging_service.repository.LogRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class LogConsumerService {

    private final LogRepository logRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "logging-topic", groupId = "logging-group")
    public void listen(String messagePayload) {
        try {

            LogMessageDto dto = objectMapper.readValue(messagePayload, LogMessageDto.class);
            LogDump logDump = new LogDump();
            logDump.setMessageType(dto.getMessageType());
            logDump.setMessage(dto.getMessage());
            
            if (dto.getDateTime() != null) {
                logDump.setDateTime(dto.getDateTime());
            }

            logRepository.save(logDump);
            log.info("Saved log entry of type: {}", dto.getMessageType());

        } catch (Exception e) {
            log.error("Failed to process incoming log message: {}", messagePayload, e);
        }
    }
}