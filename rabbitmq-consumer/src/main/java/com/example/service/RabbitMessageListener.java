package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class RabbitMessageListener {
    private final MessageValidator messageValidator;

    public RabbitMessageListener(MessageValidator messageValidator){
        this.messageValidator = messageValidator;
    }

    // DIRECT
    @RabbitListener(queues = "${orders.queue}")
    public void consumeOrder(OrderDTO order){
        messageValidator.validateOrder(order);

        try{
            log.info("✅ Received message is: product={}, quantity={}", order.getProduct(), order.getQuantity());

            //TODO you can customize processOrder(OrderDTO order) for business logic

        }catch (Exception e){
            log.error("❌ Failed to process order: {}", order, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    @RabbitListener(queues = "${notification.queue}")
    public void consumeNotification(NotificationDTO notification){
        messageValidator.validateNotification(notification);

        try{
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            //TODO you can customize processNotification(NotificationDTO notification) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }


    // FANOUT
    @RabbitListener(queues = "${email.queue}")
    public void consumeEmailNotification(NotificationDTO notification){
        messageValidator.validateNotification(notification);

        try{
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            //TODO you can customize processEmailNotification(NotificationDTO notification) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    @RabbitListener(queues = "${sms.queue}")
    public void consumeSmsNotification(NotificationDTO notification){
        messageValidator.validateNotification(notification);

        try{
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            //TODO you can customize processSmsNotification(NotificationDTO notification) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    @RabbitListener(queues = "${push.queue}")
    public void consumePushNotification(NotificationDTO notification){
        messageValidator.validateNotification(notification);

        try{
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            //TODO you can customize processPushNotification(NotificationDTO notification) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }


    //  TOPIC
    @RabbitListener(queues = "${user.signup.queue}")
    public void consumeUserSignUpQueue(UserEventDTO userEvent){
        messageValidator.validateUserEvent(userEvent);

        try{
            log.info("✅ Received message is: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());

            //TODO you can customize processSignUpQueue(UserEventDTO userEvent) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", userEvent, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    @RabbitListener(queues = "${user.login.queue}")
    public void consumeUserLoginQueue(UserEventDTO userEvent){
        messageValidator.validateUserEvent(userEvent);

        try{
            log.info("✅ Received message is: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());

            //TODO you can customize processLoginQueue(UserEventDTO userEvent) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", userEvent, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    @RabbitListener(queues = "${system.error.queue}")
    public void consumeSystemErrorQueue(SystemEventDTO systemEvent){
        messageValidator.validateSystemError(systemEvent);

        try{
            log.info("✅ Received message is: component={}, severity={}, errorCode={}, message={}, createdAt={}, metadata={}",
                    systemEvent.getComponent(), systemEvent.getSeverity(), systemEvent.getErrorCode(),
                    systemEvent.getMessage(), systemEvent.getCreatedAt(), systemEvent.getMetadata());

            //TODO you can customize processLoginQueue(SystemEventDTO systemEvent) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", systemEvent, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }
    }

    // HEADERS
    @RabbitListener(queues = "${priority.high.queue}")
    public void consumeHighPriorityMessage(PriorityMessageDTO message) {
        messageValidator.validatePriorityMessage(message);
        log.info("🔴 HIGH PRIORITY: {}", message);
        // Business logic
    }

    @RabbitListener(queues = "${priority.low.queue}")
    public void consumeLowPriorityMessage(PriorityMessageDTO message) {
        log.info("🟢 LOW PRIORITY: {}", message);
        // Business logic
    }

//    @RabbitListener(queues = "${priority.low.queue}")
//    public void consumeLowPriorityMessage(PriorityMessageDTO message, Message amqpMessage) {
//        Map<String, Object> headers = amqpMessage.getMessageProperties().getHeaders();
//        log.info("🟢 LOW PRIORITY MESSAGE");
//        log.info("Headers: {}", headers);
//        log.info("Priority header: {}", headers.get("priority"));
//        log.info("Message: {}", message);
//    }
}