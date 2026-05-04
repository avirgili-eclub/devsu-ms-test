package com.devsu.msidentity.infrastructure.messaging.event;

import java.time.LocalDateTime;

public record ClientCreatedEvent(String eventId, Long clientId, LocalDateTime occurredAt) {}
