package com.devsu.msfinance.domain.port.out;

import com.devsu.msfinance.domain.model.ProcessedEvent;

public interface ProcessedEventRepository {

    boolean existsByEventId(String eventId);

    ProcessedEvent save(ProcessedEvent event);
}
