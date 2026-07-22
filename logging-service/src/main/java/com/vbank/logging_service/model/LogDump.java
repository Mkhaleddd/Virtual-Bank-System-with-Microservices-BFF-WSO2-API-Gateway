package com.vbank.logging_service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "log_dump")
@Data
public class LogDump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(nullable = false)
    private String messageType; // "Request" or "Response"

    @Column(nullable = false)
    private Instant dateTime;
}