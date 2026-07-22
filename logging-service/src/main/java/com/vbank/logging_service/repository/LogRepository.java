package com.vbank.logging_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vbank.logging_service.model.LogDump;

public interface LogRepository extends JpaRepository<LogDump, Long>{

}
