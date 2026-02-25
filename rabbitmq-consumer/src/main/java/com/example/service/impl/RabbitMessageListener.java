package com.example.service.impl;

import com.example.configuration.MessageValidator;
import com.example.dto.*;
import com.example.enums.ChannelType;
import com.example.service.MessagingApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMessageListener {
    private final MessagingApplicationService messagingApplicationService;

    public RabbitMessageListener(MessagingApplicationService messagingApplicationService){
        this.messagingApplicationService = messagingApplicationService;
    }

    // DIRECT
    @RabbitListener(queues = "${orders.queue}")
    public void consumeOrder(OrderDTO order){
        try{
            messagingApplicationService.handleOrder(order);
        }
        catch (IllegalArgumentException e){
            log.error("❌ Failed to process notification: {}", order, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${notification.queue}")
    public void consumeNotification(NotificationDTO notification){
        try{
            messagingApplicationService.handleNotification(notification, ChannelType.NOTIFICATION);

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }


    // FANOUT
    @RabbitListener(queues = "${email.queue}")
    public void consumeEmailNotification(NotificationDTO notification){
        try{
            messagingApplicationService.handleNotification(notification, ChannelType.EMAIL);

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${sms.queue}")
    public void consumeSmsNotification(NotificationDTO notification){
        try{
            messagingApplicationService.handleNotification(notification, ChannelType.SMS);

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${push.queue}")
    public void consumePushNotification(NotificationDTO notification){
        try{
            messagingApplicationService.handleNotification(notification, ChannelType.PUSH);

        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process notification: {}", notification, e);
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }


    //  TOPIC
    @RabbitListener(queues = "${user.signup.queue}")
    public void consumeUserSignUpQueue(UserEventDTO userEvent){
        try{
            messagingApplicationService.handleUserEvent(userEvent, ChannelType.SIGNUP);
        }catch (IllegalArgumentException e){
            log.error("❌ Failed to process user signup: {}", userEvent, e);

            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${user.login.queue}")
    public void consumeUserLoginQueue(UserEventDTO userEvent){
        try{
            messagingApplicationService.handleUserEvent(userEvent, ChannelType.LOGIN);
        }catch (IllegalArgumentException e){
            log.error("❌ Failed to process user signup: {}", userEvent, e);

            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${system.error.queue}")
    public void consumeSystemErrorQueue(SystemEventDTO systemEvent){
        try{
            messagingApplicationService.handleSystemErrorMessage(systemEvent);
        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process system event: {}", systemEvent, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }

    }

    // HEADERS
    @RabbitListener(queues = "${priority.high.queue}")
    public void consumeHighPriorityMessage(PriorityMessageDTO message) {
        try{
            messagingApplicationService.handlePriorityMessage(message, ChannelType.HIGH);
        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process high priority message: {}", message, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

    @RabbitListener(queues = "${priority.low.queue}")
    public void consumeLowPriorityMessage(PriorityMessageDTO message) {
        try{
            messagingApplicationService.handlePriorityMessage(message, ChannelType.LOW);
        }catch (IllegalArgumentException e) {
            log.error("❌ Failed to process low priority message: {}", message, e);
            // *
            throw new AmqpRejectAndDontRequeueException("Invalid message payload", e);
        }
    }

}

//TODO * - invalid payload -> DON'T retry, send to DLQ (if DLX is configured)
//TODO ** - Here you can retry to send your message