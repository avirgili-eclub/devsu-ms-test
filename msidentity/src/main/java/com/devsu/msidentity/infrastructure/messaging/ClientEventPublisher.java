package com.devsu.msidentity.infrastructure.messaging;

import com.devsu.msidentity.infrastructure.messaging.event.ClientDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClientEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishClientDeleted(Long clientId) {
        ClientDeletedEvent event = new ClientDeletedEvent(
                UUID.randomUUID().toString(),
                clientId,
                LocalDateTime.now()
        );
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.CLIENT_EVENTS_EXCHANGE,
                    RabbitMqConfig.CLIENT_DELETED_ROUTING_KEY,
                    event
            );
            log.info("Published ClientDeletedEvent: eventId={}, clientId={}", event.eventId(), clientId);
        } catch (Exception e) {
            log.error("Failed to publish ClientDeletedEvent for clientId={}: {}", clientId, e.getMessage());
        }
    }
}
