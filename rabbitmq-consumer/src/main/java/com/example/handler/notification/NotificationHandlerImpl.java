package com.example.handler.notification;

import com.example.dto.NotificationDTO;
import com.example.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationHandlerImpl implements NotificationHandler {
    @Override
    public ChannelType getChannel() {
        return ChannelType.NOTIFICATION;
    }

    @Override
    public void handleNotification(NotificationDTO notification) {
        log.info("✅ notification.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
        // *** processNotification()
    }
}
