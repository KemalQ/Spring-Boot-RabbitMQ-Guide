package com.example.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Configuration
public class DirectExchangeConfig {
    //DIRECT

    @Value("${direct.exchange}")
    private String directExchange;

    @Value("${orders.queue}")
    private  String ordersQueue;
    @Value("${orders.route}")
    private  String ordersRoute;

    @Value("${notification.queue}")
    private  String notificationQueue;
    @Value("${notification.route}")
    private  String notificationRoute;

    //EXCHANGE BEAN DECLARATION

    @Bean
    public DirectExchange directExchangeMethod(){
        return new DirectExchange(directExchange, true, true);
    }


    //QUEUES

    @Bean
    public Queue ordersQueue(){
        return new Queue(ordersQueue, true);
    }
    @Bean
    public Queue notificationQueue(){
        return new Queue(notificationQueue, true);
    }


    //BINDINGS

    @Bean
    public Binding ordersBinding(){
        return BindingBuilder.bind(ordersQueue())
                .to(directExchangeMethod())
                .with(ordersRoute);
    }

    @Bean
    public Binding notificationBinding(){
        return BindingBuilder.bind(notificationQueue())
                .to(directExchangeMethod())
                .with(notificationRoute);
    }
}
