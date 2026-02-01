package com.example.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Configuration
public class TopicExchangeConfig {
    //TOPIC

    @Value("${topic.exchange}")
    private String topicExchange;

    @Value("${user.signup.queue}")
    private String userSignUpQueue;
    @Value("${user.login.queue}")
    private String userLoginQueue;
    @Value("${system.error.queue}")
    private String systemErrorQueue;

    //EXCHANGE BEAN DECLARATION
    @Bean
    public TopicExchange topicExchangeMethod(){
        return new TopicExchange(topicExchange, true, false);
    }


    //QUEUES
    @Bean
    public Queue userSignUpQueue(){
        return new Queue(userSignUpQueue, true);
    }
    @Bean
    public Queue userLoginQueue(){
        return new Queue(userLoginQueue, true);
    }
    @Bean
    public Queue systemErrorQueue(){
        return new Queue(systemErrorQueue, true);
    }


    //BINDINGS
    @Bean
    public Binding userSignUpBinding(){
        return BindingBuilder.bind(userSignUpQueue()).to(topicExchangeMethod()).with("user.#");
    }

    @Bean
    public Binding userLoginBinding(){
        return BindingBuilder.bind(userLoginQueue()).to(topicExchangeMethod()).with("login.#");
    }

    @Bean
    public Binding systemErrorBinding(){
        return BindingBuilder.bind(systemErrorQueue()).to(topicExchangeMethod()).with("error.#");
    }
}
