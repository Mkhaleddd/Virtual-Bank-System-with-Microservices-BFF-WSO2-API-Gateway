package com.vbank.account_service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountInactivationService {
    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 * * * *") // every hour, on the hour
    @Transactional
    public void inactivateStaleAccounts() {
        Instant threshold = Instant.now().minus(1, ChronoUnit.DAYS);
        int updated = accountRepository.inactivateStaleAccounts(threshold);
        log.info("Inactivation job finished. {} account(s) marked INACTIVE.", updated);
    }
}
