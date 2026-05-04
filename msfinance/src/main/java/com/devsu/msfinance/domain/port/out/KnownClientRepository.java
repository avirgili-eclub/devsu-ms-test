package com.devsu.msfinance.domain.port.out;

public interface KnownClientRepository {

    void save(Long clientId);

    void deleteByClientId(Long clientId);

    boolean existsByClientId(Long clientId);
}
