package com.example.service;

import com.example.dto.NotificationDTO;
import com.example.dto.OrderDTO;
import com.example.dto.SystemEventDTO;
import com.example.dto.UserEventDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.transform.sax.SAXResult;

@Slf4j
@Service
public class RabbitMessagePublisher {
    private final RabbitTemplate rabbitTemplate;
    private final String directExchange;
    private final String fanoutExchange;
    private final String topicExchange;

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

    public RabbitMessagePublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${direct.exchange}") String directExchange,
                                  @Value("${fanout.exchange}") String fanoutExchange,
                                  @Value("${topic.exchange}") String topicExchange){
        this.directExchange = directExchange;
        this.rabbitTemplate = rabbitTemplate;
        this.fanoutExchange = fanoutExchange;
        this.topicExchange = topicExchange;
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
}