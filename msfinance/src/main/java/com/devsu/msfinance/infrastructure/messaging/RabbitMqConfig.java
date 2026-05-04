package com.devsu.msfinance.infrastructure.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String CLIENT_EVENTS_EXCHANGE     = "devsu.client.events";
    public static final String CLIENT_DELETED_ROUTING_KEY = "client.deleted";
    public static final String CLIENT_DELETED_QUEUE       = "msfinance.client.deleted";
    public static final String DLX                        = "devsu.dlx";
    public static final String CLIENT_DELETED_DLQ         = "msfinance.client.deleted.dlq";

    @Bean
    public TopicExchange clientEventsExchange() {
        return new TopicExchange(CLIENT_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLX, true, false);
    }

    @Bean
    public Queue clientDeletedQueue() {
        return QueueBuilder.durable(CLIENT_DELETED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", CLIENT_DELETED_DLQ)
                .withArgument("x-message-ttl", 86400000)
                .build();
    }

    @Bean
    public Queue clientDeletedDlq() {
        return QueueBuilder.durable(CLIENT_DELETED_DLQ).build();
    }

    @Bean
    public Binding clientDeletedBinding(Queue clientDeletedQueue, TopicExchange clientEventsExchange) {
        return BindingBuilder.bind(clientDeletedQueue).to(clientEventsExchange).with(CLIENT_DELETED_ROUTING_KEY);
    }

    @Bean
    public Binding dlqBinding(Queue clientDeletedDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(clientDeletedDlq).to(deadLetterExchange).with(CLIENT_DELETED_DLQ);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
