package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityMessageDTO {
    private String source;        // SERVICE_NAME
    private String message;
    private String level;         // BUSINESS / SYSTEM
    private Instant createdAt;
}