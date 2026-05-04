package com.devsu.msidentity.infrastructure.messaging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateCustomizer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {

    public static final String CLIENT_EVENTS_EXCHANGE     = "devsu.client.events";
    public static final String CLIENT_CREATED_ROUTING_KEY = "client.created";
    public static final String CLIENT_DELETED_ROUTING_KEY = "client.deleted";

    @Bean
    public TopicExchange clientEventsExchange() {
        return new TopicExchange(CLIENT_EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplateCustomizer rabbitTemplateCustomizer() {
        return template -> {
            template.setMandatory(true);
            template.setConfirmCallback((correlationData, ack, cause) -> {
                if (!ack) {
                    log.warn("Message not confirmed by broker, cause: {}", cause);
                }
            });
            template.setReturnsCallback(returned ->
                log.warn("Message returned — unroutable: routingKey={}", returned.getRoutingKey())
            );
        };
    }
}
