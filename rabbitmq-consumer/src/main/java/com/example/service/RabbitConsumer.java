package com.example.service;

import com.example.configuration.MessageValidator;
import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitConsumer {
    private final MessageValidator messageValidator;

    public RabbitConsumer(MessageValidator messageValidator){
        this.messageValidator = messageValidator;
    }

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

            //TODO you can customize sendNotification(NotificationDTO notification) for business logic here

        }catch (Exception e){
            log.error("❌ Failed to process notification: {}", notification, e);
            //TODO Here you can send your message to Dead Letter Queue or retry
            throw e;
        }

    }


}


/*
    @RabbitListener(queues = "${queue.name}")
    public void consume(String message){
        log.info("Received message is- " + message);
    }
 */
/*
    private void processOrder(OrderDTO order){
        //TODO make business logic layer
        log.info("Order processing logic executed");
    }

    private void sendNotification(NotificationDTO notification){
        //TODO make business logic layer
        log.info("Notification processing logic executed");
    }
 */



























//    @RabbitListener(queues = ("${queue.name}"))
//    public void consume(String text){
//        log.info("message " + text + " received from queue");
//    }