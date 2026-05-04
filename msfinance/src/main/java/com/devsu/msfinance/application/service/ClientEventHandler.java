package com.devsu.msfinance.application.service;

import com.devsu.msfinance.domain.model.ProcessedEvent;
import com.devsu.msfinance.domain.port.in.AccountUseCase;
import com.devsu.msfinance.domain.port.out.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientEventHandler {

    private final AccountUseCase accountUseCase;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public boolean handleClientDeleted(String eventId, Long clientId) {
        if (processedEventRepository.existsByEventId(eventId)) {
            log.info("Duplicate event skipped: eventId={}", eventId);
            return false;
        }
        processedEventRepository.save(new ProcessedEvent(eventId, LocalDateTime.now()));
        accountUseCase.deactivateByClientId(clientId);
        log.info("Client accounts deactivated: clientId={}", clientId);
        return true;
    }
}
