package com.devsu.msfinance.infrastructure.persistence;

import com.devsu.msfinance.domain.model.Movement;
import com.devsu.msfinance.domain.port.out.MovementRepository;
import com.devsu.msfinance.infrastructure.persistence.jpa.MovementJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovementRepositoryAdapter implements MovementRepository {

    private final MovementJpaRepository jpa;

    @Override
    public Movement save(Movement movement) {
        return jpa.save(movement);
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Movement> findAll() {
        return jpa.findAll();
    }

    @Override
    public List<Movement> findByAccountId(Long accountId) {
        return jpa.findByAccount_Id(accountId);
    }

    @Override
    public List<Movement> findByClientIdAndDateBetween(Long clientId, LocalDateTime from, LocalDateTime to) {
        return jpa.findByAccount_ClientIdAndDateBetween(clientId, from, to);
    }
}
