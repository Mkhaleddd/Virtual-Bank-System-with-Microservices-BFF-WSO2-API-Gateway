package com.vbank.logging_service.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Index;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(
    name = "log_dump",
    schema = "logging_schema",
    indexes = {
        @Index(name = "idx_log_type_datetime", columnList = "message_type, date_time"),
        @Index(name = "idx_log_account_id", columnList = "account_id"),
        @Index(name = "idx_log_event_type", columnList = "event_type")
    }
)
@Data
public class LogDump {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "message_type", nullable = false)
    private String messageType;

    @Column(name = "date_time", nullable = false)
    private Instant dateTime;
}