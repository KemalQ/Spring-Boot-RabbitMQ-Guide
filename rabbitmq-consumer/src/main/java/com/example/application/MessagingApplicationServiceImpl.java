package com.example.application;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.enums.UserEventType;
import com.example.handler.notification.NotificationHandler;
import com.example.handler.notification.NotificationHandlerRegistry;
import com.example.handler.userEvent.UserEventHandler;
import com.example.handler.userEvent.UserEventHandlerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagingApplicationServiceImpl implements MessagingApplicationService {
    private final MessageValidator messageValidator;
    private final NotificationHandlerRegistry registry;
    private final UserEventHandlerRegistry eventHandlerRegistry;

    public MessagingApplicationServiceImpl(MessageValidator messageValidator, NotificationHandlerRegistry registry, UserEventHandlerRegistry eventHandlerRegistry) {
        this.messageValidator = messageValidator;
        this.registry = registry;
        this.eventHandlerRegistry = eventHandlerRegistry;
    }


    @Override
    public void handleOrder(OrderDTO order) {
        messageValidator.validateOrder(order);

        log.info("✅ orders.queue message: product={}, quantity={}", order.getProduct(), order.getQuantity());
        // ***
    }

    @Override
    public void handleNotification(NotificationDTO notification, ChannelType channelType) {

        /// Strategy pattern

        messageValidator.validateNotification(notification);

        NotificationHandler handler = registry.get(channelType);

        if (handler == null){
            throw new IllegalArgumentException("Unsupported channel type: " + channelType);
        }

        handler.handleNotification(notification);

    }

    @Override
    public void handleUserEvent(UserEventDTO userEvent, UserEventType eventType) {

        /// Strategy pattern

        messageValidator.validateUserEvent(userEvent);

        UserEventHandler handler = eventHandlerRegistry.get(eventType);

        if (handler == null){
            throw new IllegalArgumentException("Unsupported channel type: " + eventType);
        }
        handler.handleUserEvent(userEvent);

    }

    @Override
    public void handleSystemErrorMessage(SystemEventDTO systemEvent) {
        messageValidator.validateSystemErrorEvent(systemEvent);
        log.info("✅ system.error.queue message: component={}, severity={}, errorCode={}, message={}, createdAt={}, metadata={}",
                systemEvent.getComponent(), systemEvent.getSeverity(), systemEvent.getErrorCode(),
                systemEvent.getMessage(), systemEvent.getCreatedAt(), systemEvent.getMetadata());

        // ***
    }

    @Override
    public void handlePriorityMessage(PriorityMessageDTO message, ChannelType channelType) {
        messageValidator.validatePriorityMessage(message);

        if (channelType == ChannelType.HIGH){
            log.info("✅ priority.high.queue message: {}", message);
            // ***
        }
        if (channelType == ChannelType.LOW){
            log.info("✅ priority.low.queue message: {}", message);
            // ***
        }
    }
}

//TODO *** - Here you can customize process() for business logic here. Example:
// processOrder(OrderDTO order)
// processLoginQueue(SystemEventDTO systemEvent)
// processLoginQueue(SystemEventDTO systemEvent)