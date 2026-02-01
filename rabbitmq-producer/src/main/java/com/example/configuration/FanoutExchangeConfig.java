package com.example.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Setter
@Configuration
public class FanoutExchangeConfig {
    //FANOUT

    @Value("${fanout.exchange}")
    private String fanoutExchange;

    @Value("${email.queue}")
    private String emailQueue;
    @Value("${sms.queue}")
    private String smsQueue;
    @Value("${push.queue}")
    private String pushQueue;


    //EXCHANGE BEAN DECLARATION
    @Bean
    public FanoutExchange fanoutExchangeMethod(){
        return new FanoutExchange(fanoutExchange, true, false);
    }


    //QUEUES
    @Bean
    public Queue emailQueue(){
        return new Queue(emailQueue, true);
    }

    @Bean
    public Queue smsQueue(){
        return new Queue(smsQueue, true);
    }

    @Bean
    public Queue pushQueue(){
        return new Queue(pushQueue, true);
    }


    //BINDING BEAN DECLARATION
    @Bean
    public Binding emailBinding(){
        return BindingBuilder.bind(emailQueue()).to(fanoutExchangeMethod());
    }

    @Bean
    public Binding smsBinding(){
        return BindingBuilder.bind(smsQueue()).to(fanoutExchangeMethod());
    }

    @Bean
    public Binding pushBinding(){
        return BindingBuilder.bind(pushQueue()).to(fanoutExchangeMethod());
    }
}
