package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
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
        try{
            messageValidator.validateOrder(order);

            log.info("✅ Received message is: product={}, quantity={}", order.getProduct(), order.getQuantity());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process order: {}", order, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process order: {}", order, e);
            // **
            throw e;
        }
    }

    @RabbitListener(queues = "${notification.queue}")
    public void consumeNotification(NotificationDTO notification){
        try{
            messageValidator.validateNotification(notification);
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            // **
            throw e;
        }
    }


    // FANOUT
    @RabbitListener(queues = "${email.queue}")
    public void consumeEmailNotification(NotificationDTO notification){
        try{
            messageValidator.validateNotification(notification);
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //  **
            throw e;
        }
    }

    @RabbitListener(queues = "${sms.queue}")
    public void consumeSmsNotification(NotificationDTO notification){
        try{
            messageValidator.validateNotification(notification);
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            // **
            throw e;
        }
    }

    @RabbitListener(queues = "${push.queue}")
    public void consumePushNotification(NotificationDTO notification){
        try{
            messageValidator.validateNotification(notification);
            log.info("✅ Received message is: userId={}, notification={}", notification.getUserId(), notification.getMessage());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            // **
            throw e;
        }
    }


    //  TOPIC
    @RabbitListener(queues = "${user.signup.queue}")
    public void consumeUserSignUpQueue(UserEventDTO userEvent){
        try{
            messageValidator.validateUserEvent(userEvent);
            log.info("✅ Received message is: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process user signup: {}", userEvent, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process user signup: {}", userEvent, e);
            // **
            throw e;
        }
    }

    @RabbitListener(queues = "${user.login.queue}")
    public void consumeUserLoginQueue(UserEventDTO userEvent){
        try{
            messageValidator.validateUserEvent(userEvent);
            log.info("✅ Received message is: eventType={}, userId={}, username={}, email={}, timestamp={}, ipAddress={}",
                    userEvent.getEventType(), userEvent.getUserId(), userEvent.getUsername(),
                    userEvent.getEmail(), userEvent.getOccurredAt(), userEvent.getIpAddress());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process user login: {}", userEvent, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process user login: {}", userEvent, e);
            // **
            throw e;
        }
    }

    @RabbitListener(queues = "${system.error.queue}")
    public void consumeSystemErrorQueue(SystemEventDTO systemEvent){
        try{
            messageValidator.validateSystemErrorEvent(systemEvent);
            log.info("✅ Received message is: component={}, severity={}, errorCode={}, message={}, createdAt={}, metadata={}",
                    systemEvent.getComponent(), systemEvent.getSeverity(), systemEvent.getErrorCode(),
                    systemEvent.getMessage(), systemEvent.getCreatedAt(), systemEvent.getMetadata());

            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process system event: {}", systemEvent, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process system event: {}", systemEvent, e);
            // **
            throw e;
        }
    }

    // HEADERS
    @RabbitListener(queues = "${priority.high.queue}")
    public void consumeHighPriorityMessage(PriorityMessageDTO message) {
        try{
            messageValidator.validatePriorityMessage(message);
            log.info("🔴 HIGH PRIORITY: {}", message);
            // ***

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process high priority message: {}", message, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process high priority message: {}", message, e);
            // **
            throw e;
        }

    }

    @RabbitListener(queues = "${priority.low.queue}")
    public void consumeLowPriorityMessage(PriorityMessageDTO message) {
        try{
            messageValidator.validatePriorityMessage(message);
            log.info("🟢 LOW PRIORITY: {}", message);
            // ***
        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process low priority message: {}", message, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }catch (Exception e){
            log.error("❌ Failed to process low priority message: {}", message, e);
            // **
            throw e;
        }

    }

}

//TODO *** - Here you can customize process() for business logic here. Example:
// processOrder(OrderDTO order)
// processLoginQueue(SystemEventDTO systemEvent)
// processLoginQueue(SystemEventDTO systemEvent)

//TODO * - invalid payload -> DON'T retry, send to DLQ (if DLX is configured)
//TODO ** - Here you can retry to send your message