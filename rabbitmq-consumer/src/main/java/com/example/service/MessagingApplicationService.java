package com.example.service;

import com.example.dto.*;
import com.example.enums.ChannelType;

public interface MessagingApplicationService {
    // DIRECT EXCHANGE
    void handleOrder(OrderDTO order);
    void handleNotification(NotificationDTO notification, ChannelType channelType);

    // TOPIC EXCHANGE
    void handleUserEvent(UserEventDTO userEvent, ChannelType channelType);
    void handleSystemErrorMessage(SystemEventDTO systemEvent);


    // HEADERS EXCHANGE
    void handlePriorityMessage(PriorityMessageDTO priorityMessage, ChannelType channelType);

}
