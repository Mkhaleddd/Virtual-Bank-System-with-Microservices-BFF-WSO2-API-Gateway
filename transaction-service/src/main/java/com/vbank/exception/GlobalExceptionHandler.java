package com.vbank.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        response.put("error", "Bad Request");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(NotFoundException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", "Not Found");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class) // fallback for all exceptions
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, Object>> handleConflictException(ConflictException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", "Conflict");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        Map<String, Object> response = new HashMap<>();

        response.put("error", "Unauthorized");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.UNAUTHORIZED.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientFundsException(InsufficientFundsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Insufficient Funds");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Map<String, Object>> handleDownstreamException(HttpStatusCodeException ex) {
        Map<String, Object> response = new HashMap<>();
        String downstreamMessage;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> parsed = mapper.readValue(
                    ex.getResponseBodyAsString(),
                    new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
            downstreamMessage = String.valueOf(parsed.getOrDefault("message", ex.getResponseBodyAsString()));
        } catch (Exception parseEx) {
            downstreamMessage = ex.getResponseBodyAsString();
        }

        response.put("error", HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase());
        response.put("message", downstreamMessage);
        response.put("status", ex.getStatusCode().value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatusCode().value()));
    }

}
