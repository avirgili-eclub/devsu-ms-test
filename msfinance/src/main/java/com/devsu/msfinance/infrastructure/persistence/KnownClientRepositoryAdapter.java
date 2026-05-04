package com.devsu.msfinance.infrastructure.persistence;

import com.devsu.msfinance.domain.model.KnownClient;
import com.devsu.msfinance.domain.port.out.KnownClientRepository;
import com.devsu.msfinance.infrastructure.persistence.jpa.KnownClientJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KnownClientRepositoryAdapter implements KnownClientRepository {

    private final KnownClientJpaRepository jpa;

    @Override
    public void save(Long clientId) {
        jpa.save(new KnownClient(clientId));
    }

    @Override
    public void deleteByClientId(Long clientId) {
        jpa.deleteById(clientId);
    }

    @Override
    public boolean existsByClientId(Long clientId) {
        return jpa.existsById(clientId);
    }
}
