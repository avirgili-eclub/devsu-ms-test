package com.devsu.msfinance.application.service;

import com.devsu.msfinance.domain.port.out.KnownClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCreatedHandler {

    private final KnownClientRepository knownClientRepository;

    @Transactional
    public void handleClientCreated(Long clientId) {
        if (knownClientRepository.existsByClientId(clientId)) {
            log.info("Client already registered: clientId={}", clientId);
            return;
        }
        knownClientRepository.save(clientId);
        log.info("Client registered in local registry: clientId={}", clientId);
    }
}
