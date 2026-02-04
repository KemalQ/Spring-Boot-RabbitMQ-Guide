package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemEventDTO {
    private String component;      // "AUTH_SERVICE", "PAYMENT_SERVICE"
    private String severity;       // "ERROR", "WARNING", "INFO"
    private String errorCode;      // "SYS_001", "DB_TIMEOUT"
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> metadata;  // additional text
}
