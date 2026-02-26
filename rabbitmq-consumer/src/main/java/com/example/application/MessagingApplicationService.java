package com.example.application;

import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.enums.UserEventType;

public interface MessagingApplicationService {
    // DIRECT EXCHANGE
    void handleOrder(OrderDTO order);
    void handleNotification(NotificationDTO notification, ChannelType channelType);

    // TOPIC EXCHANGE
    void handleUserEvent(UserEventDTO userEvent, UserEventType channelType);
    void handleSystemErrorMessage(SystemEventDTO systemEvent);


    // HEADERS EXCHANGE
    void handlePriorityMessage(PriorityMessageDTO priorityMessage, ChannelType channelType);

}
