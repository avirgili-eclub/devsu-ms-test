package com.devsu.msfinance.domain.port.out;

import com.devsu.msfinance.domain.model.Movement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovementRepository {

    Movement save(Movement movement);

    Optional<Movement> findById(Long id);

    List<Movement> findAll();

    List<Movement> findByAccountId(Long accountId);

    List<Movement> findByClientIdAndDateBetween(Long clientId, LocalDateTime from, LocalDateTime to);

    void deleteByAccountId(Long accountId);
}
