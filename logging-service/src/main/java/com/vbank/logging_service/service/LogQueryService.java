package com.vbank.logging_service.service;

import com.vbank.logging_service.model.LogDump;
import com.vbank.logging_service.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogQueryService {

    private final LogRepository logRepository;

    public Page<LogDump> searchLogs(String messageType, String keyword, Pageable pageable) {
        return logRepository.searchLogs(messageType, keyword, pageable);
    }
}