package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import com.example.dto.UserEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

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


    //FANOUT

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
    public void consumeSignUpQueue(UserEventDTO userEvent){
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
}