package com.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO {
    @NotBlank(message = "Event type cannot be empty")
    private String eventType;      // "SIGNUP", "LOGIN", "LOGOUT"

    @NotNull(message = "User ID cannot be empty")
    private Long userId;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    private String email;

    @NotNull(message = "Event Occurred time cannot be null")
    private Instant occurredAt;

    @NotBlank(message = "IP Address cannot be empty")
    private String ipAddress;
}
