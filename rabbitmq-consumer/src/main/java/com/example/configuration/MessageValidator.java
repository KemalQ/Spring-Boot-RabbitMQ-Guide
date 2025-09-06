package com.example.configuration;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
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
}
