package com.example.service;

import com.example.dto.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Service
@Validated
public class RabbitMessagePublisher {
    private final RabbitTemplate rabbitTemplate;

    private final String directExchange;
    private final String fanoutExchange;
    private final String topicExchange;
    private final String headersExchange;

    //  DIRECT
    @Value("${orders.route}")
    private String ordersRoute;
    @Value("${notification.route}")
    private String notificationRoute;

    //TOPIC
    @Value("${user.signup.route}")
    private String userSignup;
    @Value("${user.login.route}")
    private String userLogin;
    @Value("${system.error.route}")
    private String systemError;

    //HEADERS
    @Value("${priority.high.queue}")
    private String priorityHighQueue;


    public RabbitMessagePublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${direct.exchange}") String directExchange,
                                  @Value("${fanout.exchange}") String fanoutExchange,
                                  @Value("${topic.exchange}") String topicExchange,
                                  @Value("${headers.exchange}") String headersExchange){
        this.directExchange = directExchange;
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
        this.topicExchange = topicExchange;
        this.headersExchange = headersExchange;
    }

    //  DIRECT
    public void sendOrder(@Valid OrderDTO order){
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ Orders message sending to {} exchange, with {} key, with id: {}",
                directExchange, ordersRoute, correlationData.getId());
        rabbitTemplate.convertAndSend(directExchange, ordersRoute, order, correlationData);
    }

    public void sendNotification(@Valid NotificationDTO notificationDTO){
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ Notifications message sending to {} exchange, with {} key, with id: {}",
                directExchange, notificationRoute, correlationData.getId());
        rabbitTemplate.convertAndSend(directExchange, notificationRoute, notificationDTO, correlationData);
    }

    //  FANOUT
    public void sendFanoutNotification(@Valid NotificationDTO notification) {
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ Notifications message sending to {} exchange, with id: {}",
                fanoutExchange, correlationData.getId());
        rabbitTemplate.convertAndSend(fanoutExchange, "", notification, correlationData);
    }

    //  TOPIC
    public void sendUserSignup(@Valid UserEventDTO userEvent){
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ User Event message sending to {} exchange, with id: {}",
                topicExchange, correlationData.getId());
        rabbitTemplate.convertAndSend(topicExchange, userSignup, userEvent, correlationData);
    }

    public void sendUserLogin(@Valid UserEventDTO userEvent){
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ User SignUp message sending to {} exchange, with id: {}",
                topicExchange, correlationData.getId());
        rabbitTemplate.convertAndSend(topicExchange, userLogin, userEvent, correlationData);
    }

    public void sendSystemError(@Valid SystemEventDTO systemEvent){
        CorrelationData correlationData = createCorrelationData();

        log.info("✅ User System Error message sending to {} exchange, with id: {}",
                topicExchange, correlationData.getId());
        rabbitTemplate.convertAndSend(topicExchange, systemError, systemEvent, correlationData);
    }

    public void sendPriorityMessage(@Valid PriorityMessageDTO priorityMessage, String priority) {
        CorrelationData correlationData = createCorrelationData();

        rabbitTemplate.convertAndSend(headersExchange, "",  priorityMessage,
                message -> {
                    message.getMessageProperties().setHeader("priority", priority);
                    log.info("✅ {} Priority message message sending to {}, with id: {}",
                            priority, headersExchange, correlationData.getId());
                    return message;
                }, correlationData);
    }

    private CorrelationData createCorrelationData() {
        return new CorrelationData(UUID.randomUUID().toString());
    }
}