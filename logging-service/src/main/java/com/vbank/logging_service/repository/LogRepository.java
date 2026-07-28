package com.vbank.logging_service.repository;

import com.vbank.logging_service.model.LogDump;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LogRepository extends JpaRepository<LogDump, Long> {

    @Query("SELECT l FROM LogDump l WHERE " +
           "(:messageType IS NULL OR l.messageType = :messageType) AND " +
           "(:keyword IS NULL OR LOWER(l.message) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<LogDump> searchLogs(
            @Param("messageType") String messageType,
            @Param("keyword") String keyword,
            Pageable pageable);

    int deleteByDateTimeBefore(Instant thresholdDate);
}