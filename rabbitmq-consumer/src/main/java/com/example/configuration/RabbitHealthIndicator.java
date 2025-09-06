package com.example.configuration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class RabbitHealthIndicator implements HealthIndicator{
    private final RabbitTemplate rabbitTemplate;

    public RabbitHealthIndicator(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public Health health(){
        try {
            rabbitTemplate.execute(channel -> {
                channel.queueDeclarePassive("orders.queue");
                return null;
            });
            return Health.up().withDetail("rabbitmq", "Connection successful").build();
        } catch (Exception e) {
            return Health.down().withException(e).build();
        }
    }
}
