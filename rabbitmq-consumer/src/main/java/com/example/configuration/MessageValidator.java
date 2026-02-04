package com.example.configuration;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import com.example.dto.UserEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageValidator {
    public void validateOrder(OrderDTO order){
        if (order==null){
            log.error("✅❌ Received null order");
            return;
        }
        if (order.getProduct() == null || order.getQuantity() == null || order.getQuantity() <= 0) {
            log.error("✅❌Invalid order data: {}", order);
            throw new IllegalArgumentException("Invalid order data");
        }
    }

    public void validateNotification(NotificationDTO notification){
        if (notification == null){
            log.error("✅❌Received null notification");
            return;
        }
        if (notification.getUserId() == null || notification.getMessage() == null || notification.getMessage().isEmpty()){
            log.error("✅❌Invalid notification data: {}", notification);
            throw new IllegalArgumentException("Invalid notification data");
        }
    }

    public void validateUserEvent(UserEventDTO userEvent){
        if (userEvent == null){
            log.error("✅❌Received null User Event");
            return;
        }
        if (userEvent.getUserId() == null || userEvent.getEventType() == null ||
                userEvent.getUsername().isEmpty() || userEvent.getEmail().isEmpty() ||
                userEvent.getOccurredAt() == null || userEvent.getIpAddress().isEmpty()){
            log.error("✅❌Invalid User Event data: {}", userEvent);
            throw new IllegalArgumentException("Invalid notification data");
        }
    }
}
