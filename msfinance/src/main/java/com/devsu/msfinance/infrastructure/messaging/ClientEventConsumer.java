package com.devsu.msfinance.infrastructure.messaging;

import com.devsu.msfinance.application.service.ClientCreatedHandler;
import com.devsu.msfinance.application.service.ClientEventHandler;
import com.devsu.msfinance.infrastructure.messaging.event.ClientCreatedEvent;
import com.devsu.msfinance.infrastructure.messaging.event.ClientDeletedEvent;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEventConsumer {

    private final ClientCreatedHandler clientCreatedHandler;
    private final ClientEventHandler clientEventHandler;

    @RabbitListener(queues = RabbitMqConfig.CLIENT_CREATED_QUEUE)
    public void handleClientCreated(ClientCreatedEvent event,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Received ClientCreatedEvent: eventId={}, clientId={}", event.eventId(), event.clientId());
        clientCreatedHandler.handleClientCreated(event.clientId());
        channel.basicAck(deliveryTag, false);
    }

    @RabbitListener(queues = RabbitMqConfig.CLIENT_DELETED_QUEUE)
    public void handleClientDeleted(ClientDeletedEvent event,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        log.info("Received ClientDeletedEvent: eventId={}, clientId={}", event.eventId(), event.clientId());
        clientEventHandler.handleClientDeleted(event.eventId(), event.clientId());
        channel.basicAck(deliveryTag, false);
    }
}
