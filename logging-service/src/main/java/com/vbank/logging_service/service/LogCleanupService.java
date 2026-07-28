package com.vbank.logging_service.service;

import com.vbank.logging_service.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogCleanupService {

    private final LogRepository logRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void purgeOldLogs() {

        Instant thresholdDate = Instant.now().minus(30, ChronoUnit.DAYS);

        int deletedCount = logRepository.deleteByDateTimeBefore(thresholdDate);

        log.info("Log cleanup job finished. Successfully purged {} log entries older than 30 days.", deletedCount);
    }
}