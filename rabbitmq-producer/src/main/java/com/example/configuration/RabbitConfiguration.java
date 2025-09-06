package com.example.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Configuration
public class RabbitConfiguration {
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

    @Bean
    public DirectExchange directExchangeMethod(){
        return new DirectExchange(directExchange, true, true);
    }

    @Bean
    public Queue ordersQueue(){
        return new Queue(ordersQueue, true);
    }

    @Bean
    public Queue notificationQueue(){
        return new Queue(notificationQueue, true);
    }

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

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}

/*
@Value ("${queue.name}")
private String queueName;

@Bean
public Queue queue(){
    return new Queue(queueName, false);
}
*/

/*
    //TOPIC
    @Value("${topic.exchange}")
    private  String topicExchange;
    @Value("${topic.queue}")
    private String topicQueue;
    @Value("${topic.route}")
    private String topicRoute;

    @Bean
    public TopicExchange topicExchangeMethod(){
        return new TopicExchange(topicExchange, false, true);
    }

    @Bean
    public Queue topicQueue(){
        return new Queue(topicQueue, false);
    }

    @Bean
    public Binding topicBinding(){
        return BindingBuilder.bind(topicQueue())
                .to(topicExchangeMethod())
                .with(topicRoute);
    }

    //FANOUT
    @Value("${fanout.exchange}")
    private String fanoutExchange;
    @Value("${fanout.queue}")
    private String fanoutQueue;

    @Bean
    public FanoutExchange fanoutExchangeMethod(){
        return new FanoutExchange(fanoutExchange, false, true);
    }

    @Bean
    public Queue fanoutQueue(){
        return new Queue(fanoutQueue);
    }

    @Bean
    public Binding fanoutBinding(){
        return BindingBuilder.bind(fanoutQueue())
                .to(fanoutExchangeMethod());
    }
 */
