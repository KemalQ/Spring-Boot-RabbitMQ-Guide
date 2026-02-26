package com.example.handler.notification;

import com.example.dto.NotificationDTO;
import com.example.enums.ChannelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailHandlerImpl implements NotificationHandler {
    @Override
    public ChannelType getChannel() {
        return ChannelType.EMAIL;
    }

    @Override
    public void handleNotification(NotificationDTO notification) {
        log.info("✅ email.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
        // *** processEmail(notification);
    }
}
