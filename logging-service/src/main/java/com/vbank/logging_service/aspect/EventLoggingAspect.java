package com.vbank.logging_service.aspect;

import com.vbank.logging_service.dto.LogMessageDto;
import com.vbank.logging_service.annotation.LoggableEvent;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
public class EventLoggingAspect {

    private final KafkaTemplate<String, LogMessageDto> kafkaTemplate;

    @Around("@annotation(loggableEvent)")
    public Object logEvent(ProceedingJoinPoint joinPoint, LoggableEvent loggableEvent) throws Throwable {
        String eventType = loggableEvent.eventType();
        Long extractedAccountId = extractAccountId(joinPoint.getArgs());

        try {
            Object result = joinPoint.proceed(); 
            LogMessageDto successLog = new LogMessageDto();
            successLog.setServiceName("vbank-service");
            successLog.setEventType(eventType + "_SUCCESS");
            successLog.setAccountId(extractedAccountId);
            successLog.setMessage("Operation succeeded");
            successLog.setMessageType("AUDIT");
            successLog.setDateTime(Instant.now());

            kafkaTemplate.send("logging-topic", successLog);

            return result;

        } catch (Exception ex) {
            // On failure, ship ERROR event to Kafka
            LogMessageDto errorLog = new LogMessageDto();
            errorLog.setServiceName("vbank-service");
            errorLog.setEventType(eventType + "_FAILED");
            errorLog.setAccountId(extractedAccountId);
            errorLog.setMessage("Operation failed: " + ex.getMessage());
            errorLog.setMessageType("ERROR");
            errorLog.setDateTime(Instant.now());

            kafkaTemplate.send("logging-topic", errorLog);

            throw ex;
        }
    }

    private Long extractAccountId(Object[] args) {
        if (args == null) return null;
        for (Object arg : args) {
            if (arg == null) continue;
            try {
               
                Method method = null;
                try {
                    method = arg.getClass().getMethod("getFromAccountId");
                } catch (NoSuchMethodException e) {
                    try {
                        method = arg.getClass().getMethod("getAccountId");
                    } catch (NoSuchMethodException ignored) {}
                }

                if (method != null) {
                    Object val = method.invoke(arg);
                    if (val != null) {
                        return Long.valueOf(val.toString());
                    }
                }
            } catch (Exception ignored) {}
        }
        return null;
    }
}