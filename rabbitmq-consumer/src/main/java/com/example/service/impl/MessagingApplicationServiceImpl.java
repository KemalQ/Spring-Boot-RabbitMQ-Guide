package com.example.service.impl;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.service.MessagingApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessagingApplicationServiceImpl implements MessagingApplicationService {
    private final MessageValidator messageValidator;

    public MessagingApplicationServiceImpl(MessageValidator messageValidator) {
        this.messageValidator = messageValidator;
    }


    @Override
    public void handleOrder(OrderDTO order) {
        messageValidator.validateOrder(order);

        log.info("✅ orders.queue message: product={}, quantity={}", order.getProduct(), order.getQuantity());

        // ***
    }

    @Override
    public void handleNotification(NotificationDTO notification, ChannelType channelType) {
        messageValidator.validateNotification(notification);

        if (channelType == ChannelType.NOTIFICATION){
            log.info("✅ notification.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
            // *** processNotification(notification);
        }
        if (channelType == ChannelType.EMAIL){
            log.info("✅ email.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
            // *** processEmailNotification
        }
        if (channelType == ChannelType.PUSH){
            log.info("✅ push.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
            // *** processPushNotification
        }
        if (channelType == ChannelType.SMS){
            log.info("✅ sms.queue message: userId={}, notification={}", notification.getUserId(), notification.getMessage());
            // *** processSmsNotification
        }
    }

    @Override
    public void handleUserEvent(UserEventDTO userEvent, ChannelType channelType) {
        messageValidator.validateUserEvent(userEvent);

        if (channelType == ChannelType.LOGIN){
            log.info("✅ user.login.queue message: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());
            // *** processUserLogin(userEvent);
        }
        if (channelType == ChannelType.SIGNUP){
            log.info("✅ user.signup.queue message: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());
            // *** processUserSignup(userEvent);
        }

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