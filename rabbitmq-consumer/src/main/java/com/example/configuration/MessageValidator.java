package com.example.configuration;

import com.example.dto.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MessageValidator {
    private final Validator validator;

    public MessageValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T object, String objectType){
        if (object == null){
            log.error("✅❌Received null {}", objectType);
            throw new IllegalArgumentException(objectType + "cannot be null");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()){
            String errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            log.error("✅❌Validation failed for {}: {}", objectType, errors);
            throw new IllegalArgumentException("Invalid " + objectType + ": " + errors);
        }
        log.debug("✅ {} validation passed", objectType);
    }

    public void validateOrder(OrderDTO order){
        validate(order, "Order");
    }

    public void validateNotification(NotificationDTO notification){
        validate(notification, "Notification");
    }

    public void validateUserEvent(UserEventDTO userEvent){
        validate(userEvent, "UserEvent");
    }

    public void validateSystemErrorEvent(SystemEventDTO systemEvent) {
        validate(systemEvent, "SystemEvent");
    }

    public void validatePriorityMessage(PriorityMessageDTO priorityMessage) {
        validate(priorityMessage, "PriorityMessage");
    }
}
