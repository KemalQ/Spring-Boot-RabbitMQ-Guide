package com.example.service;

import com.example.dto.*;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
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
    public void sendOrder(OrderDTO orderDTO){
        log.info("✅ Orders message has been sent to " + directExchange + " exchange, with " + ordersRoute + "key.");
        rabbitTemplate.convertAndSend(directExchange, ordersRoute, orderDTO);
    }

    public void sendNotification(NotificationDTO notificationDTO){
        log.info("✅ Notifications message has been sent to " + directExchange + " exchange, with " + notificationRoute + "key.");
        rabbitTemplate.convertAndSend(directExchange, notificationRoute, notificationDTO);
    }

    //  FANOUT
    public void sendFanoutNotification(@Valid NotificationDTO notification) {
        log.info("✅ Notifications message has been sent to " + fanoutExchange + " exchange");
        rabbitTemplate.convertAndSend(fanoutExchange, "", notification);
    }

    //  TOPIC
    public void sendUserSignup(@Valid UserEventDTO userEvent){
        log.info("✅ User Event message has been sent to " + topicExchange + " exchange");
        rabbitTemplate.convertAndSend(topicExchange, userSignup, userEvent);
    }

    public void sendUserLogin(@Valid UserEventDTO userEvent){
        log.info("✅ User SignUp message has been sent to " + topicExchange + " exchange");
        rabbitTemplate.convertAndSend(topicExchange, userLogin, userEvent);
    }

    public void sendSystemError(@Valid SystemEventDTO systemEvent){
        log.info("✅ User System Error message has been sent to " + topicExchange + " exchange");
        rabbitTemplate.convertAndSend(topicExchange, systemError, systemEvent);
    }

    public void sendPriorityMessage(@Valid PriorityMessageDTO priorityMessage, @Valid String priority) {
        rabbitTemplate.convertAndSend(headersExchange, "",  priorityMessage,
                message -> {
                    message.getMessageProperties().setHeader("priority", priority);
                    log.info("✅ {} Priority Message message has been sent to {}", priority, headersExchange);
                    return message;
                });
    }

//    public void sendMediumPriorityMessage(@Valid PriorityMessageDTO priorityMessage, boolean urgent) {
//        rabbitTemplate.convertAndSend(headersExchange,"", priorityMessage,
//                message -> {
//                    message.getMessageProperties().setHeader("priority", "medium");
//                    message.getMessageProperties().setHeader("urgent", urgent);
//                    log.info("✅ Medium Priority Message message has been sent to {}", headersExchange);
//                    return message;
//                }
//        );
//    }
}