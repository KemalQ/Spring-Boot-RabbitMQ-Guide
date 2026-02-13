package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityMessageDTO {
    @NotBlank(message = "Source cannot be empty")
    private String source;        // SERVICE_NAME

    @NotBlank(message = "Message cannot be empty")
    private String message;

    @NotBlank(message = "Level cannot be empty")
    private String level;         // BUSINESS / SYSTEM

    @NotNull(message = "Created time cannot be null")
    private Instant createdAt;
}