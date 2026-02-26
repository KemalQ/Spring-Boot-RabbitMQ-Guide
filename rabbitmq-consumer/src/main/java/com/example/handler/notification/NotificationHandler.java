package com.example.handler.notification;

import com.example.dto.NotificationDTO;
import com.example.enums.ChannelType;

public interface NotificationHandler {

    ChannelType getChannel();

    void handleNotification(NotificationDTO notification);
}
