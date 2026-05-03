package com.devsu.msfinance.domain.port.in;

import com.devsu.msfinance.domain.model.Movement;

import java.time.LocalDate;
import java.util.List;

public interface MovementUseCase {

    Movement create(Movement movement);

    Movement getById(Long id);

    List<Movement> getAll();

    List<Movement> getByAccountId(Long accountId);

    List<Movement> getStatement(Long clientId, LocalDate from, LocalDate to);
}
