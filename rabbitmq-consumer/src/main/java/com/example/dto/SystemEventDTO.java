package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemEventDTO {
    @NotBlank(message = "Component cannot be empty")
    private String component;      // "AUTH_SERVICE", "PAYMENT_SERVICE"

    @NotBlank(message = "Severity cannot be empty")
    private String severity;       // "ERROR", "WARNING", "INFO"

    @NotBlank(message = "Error code cannot be empty")
    private String errorCode;      // "SYS_001", "DB_TIMEOUT"

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @NotNull(message = "Created time cannot be null")
    private Instant createdAt;

    @NotNull(message = "Metadata cannot be null")
    private Map<String, String> metadata;  // additional text
}