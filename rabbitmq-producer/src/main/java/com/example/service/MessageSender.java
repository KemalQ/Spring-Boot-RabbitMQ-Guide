package com.example.service;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageSender {
    private final RabbitTemplate rabbitTemplate;
    private final String directExchange;

    @Value("${orders.route}")
    private String ordersRoute;
    @Value("${notification.route}")
    private String notificationRoute;

    public MessageSender(RabbitTemplate rabbitTemplate, @Value("${direct.exchange}") String directExchange){
        this.directExchange = directExchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrder(OrderDTO orderDTO){
        log.info("✅ Orders message has been sent to " + directExchange + " exchange, with " + ordersRoute + "key.");
        rabbitTemplate.convertAndSend(directExchange, ordersRoute, orderDTO);
    }

    public void sendNotification(NotificationDTO notificationDTO){
        log.info("✅ Notifications message has been sent to " + directExchange + " exchange, with " + notificationRoute + "key.");
        rabbitTemplate.convertAndSend(directExchange, notificationRoute, notificationDTO);
    }
}