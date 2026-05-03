package com.devsu.msfinance.application.service;

import com.devsu.msfinance.domain.exception.InsufficientFundsException;
import com.devsu.msfinance.domain.exception.MovementNotFoundException;
import com.devsu.msfinance.domain.model.Account;
import com.devsu.msfinance.domain.model.Movement;
import com.devsu.msfinance.domain.port.in.MovementUseCase;
import com.devsu.msfinance.domain.port.out.AccountRepository;
import com.devsu.msfinance.domain.port.out.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementService implements MovementUseCase {

    private final MovementRepository movementRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public Movement create(Movement movement) {
        Account account = accountRepository.findById(movement.getAccount().getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found with id: " + movement.getAccount().getId()));

        BigDecimal newBalance = calculateNewBalance(account.getCurrentBalance(),
                movement.getMovementType(), movement.getAmount());

        account.setCurrentBalance(newBalance);
        accountRepository.save(account);

        movement.setDate(LocalDateTime.now());
        movement.setBalance(newBalance);
        movement.setAccount(account);

        return movementRepository.save(movement);
    }

    @Override
    public Movement getById(Long id) {
        return movementRepository.findById(id)
                .orElseThrow(() -> new MovementNotFoundException(id));
    }

    @Override
    public List<Movement> getAll() {
        return movementRepository.findAll();
    }

    @Override
    public List<Movement> getByAccountId(Long accountId) {
        return movementRepository.findByAccountId(accountId);
    }

    @Override
    public List<Movement> getStatement(Long clientId, LocalDate from, LocalDate to) {
        return movementRepository.findByClientIdAndDateBetween(
                clientId,
                from.atStartOfDay(),
                to.plusDays(1).atStartOfDay());
    }

    private BigDecimal calculateNewBalance(BigDecimal current, String type, BigDecimal amount) {
        if ("DEPOSIT".equalsIgnoreCase(type)) {
            return current.add(amount);
        }
        if (current.compareTo(amount) < 0) {
            throw new InsufficientFundsException(current, amount);
        }
        return current.subtract(amount);
    }
}
