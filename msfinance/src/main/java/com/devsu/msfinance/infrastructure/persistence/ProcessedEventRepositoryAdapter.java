package com.devsu.msfinance.infrastructure.persistence;

import com.devsu.msfinance.domain.model.ProcessedEvent;
import com.devsu.msfinance.domain.port.out.ProcessedEventRepository;
import com.devsu.msfinance.infrastructure.persistence.jpa.ProcessedEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepository {

    private final ProcessedEventJpaRepository jpa;

    @Override
    public boolean existsByEventId(String eventId) {
        return jpa.existsByEventId(eventId);
    }

    @Override
    public ProcessedEvent save(ProcessedEvent event) {
        return jpa.save(event);
    }
}
