package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
    private String eventType;      // "SIGNUP", "LOGIN", "LOGOUT"
    private Long userId;
    private String username;
    private String email;
    private Instant occurredAt;
    private String ipAddress;
}
