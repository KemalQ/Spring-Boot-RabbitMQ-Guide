package com.example.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Component
@Slf4j
public class RabbitInfrastructureInitializer {
    //  DIRECT
    @Value("${orders.queue}")
    private String ordersQueue;
    @Value("${notification.queue}")
    private String notificationQueue;

    //  FANOUT
    @Value("${email.queue}")
    private String emailQueue;
    @Value("${sms.queue}")
    private String smsQueue;
    @Value("${push.queue}")
    private String pushQueue;

    //  TOPIC
    @Value("${user.signup.queue}")
    private String userSignUpQueue;
    @Value("${user.login.queue}")
    private String userLoginQueue;
    @Value("${system.error.queue}")
    private String systemErrorQueue;

    private final RabbitAdmin rabbitAdmin;

    public RabbitInfrastructureInitializer(RabbitAdmin rabbitAdmin){
        this.rabbitAdmin = rabbitAdmin;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeMessageInfrastructure() {
        log.info("🚀 Initializing message infrastructure...");

        try {
            rabbitAdmin.initialize();

            validateQueueExist();

            log.info("✅ Message queues and exchanges created successfully");
        } catch (Exception e) {
            log.error("❌ Failed to initialize message infrastructure: {}", e.getMessage());
            throw new IllegalStateException("Message infrastructure initialization failed", e);
        }
    }
    private void validateQueueExist(){
        List<String> requiredQueue = Arrays.asList(ordersQueue, notificationQueue,
                emailQueue, smsQueue, pushQueue,
                userSignUpQueue, userLoginQueue, systemErrorQueue);

        for (String queueName : requiredQueue){
            Properties properties = rabbitAdmin.getQueueProperties(queueName);
            if (properties == null){
                throw new IllegalStateException("Required queue not found: " + queueName);
            }
            log.debug("Queue '{}' exists", queueName);
        }
    }
}
