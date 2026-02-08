package com.example.configuration;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Setter
@Configuration
public class HeaderExchangeConfig {
    //HEADERS

    @Value("${headers.exchange}")
    private String headersExchange;

    @Value("${priority.high.queue}")
    private String highPriorityQueue;
    @Value("${priority.low.queue}")
    private String lowPriorityQueue;
    @Value("${priority.medium.queue}")
    private String mediumPriorityQueue;


    //EXCHANGE BEAN DECLARATION
    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(headersExchange, true, false);
    }


    //QUEUES
    @Bean
    public Queue highPriorityQueue(){
        return new Queue(highPriorityQueue, true);
    }

    @Bean
    public Queue mediumPriorityQueue(){
        return new Queue(mediumPriorityQueue, true);
    }

    @Bean
    public Queue lowPriorityQueue(){
        return new Queue(lowPriorityQueue, true);
    }

    //BINDINGS
    @Bean
    public Binding priorityHighBindingHeaders(Queue highPriorityQueue, HeadersExchange headersExchange){
        return BindingBuilder.bind(highPriorityQueue)
                .to(headersExchange)
                .where("priority")
                .matches("high");
    }

    @Bean
    public Binding mediumPriorityBinding(
            Queue mediumPriorityQueue,
            HeadersExchange headersExchange
    ) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("priority", "medium");
        headers.put("urgent", false);

        return BindingBuilder.bind(mediumPriorityQueue)
                .to(headersExchange)
                .whereAll(headers)  // TODO check this
                .match();
    }

    @Bean
    public Binding priorityLowBinding(Queue lowPriorityQueue, HeadersExchange headersExchange){
        return BindingBuilder.bind(lowPriorityQueue)
                .to(headersExchange)
                .where("priority")
                .matches("low");
    }
}
