package com.devsu.msfinance.infrastructure.rest.mapper;

import com.devsu.msfinance.domain.model.Account;
import com.devsu.msfinance.domain.model.Movement;
import com.devsu.msfinance.infrastructure.rest.dto.MovementRequest;
import com.devsu.msfinance.infrastructure.rest.dto.MovementResponse;
import com.devsu.msfinance.infrastructure.rest.dto.StatementResponse;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper {

    public Movement toDomain(MovementRequest request) {
        Account account = Account.builder().id(request.getAccountId()).build();
        return Movement.builder()
                .account(account)
                .movementType(request.getMovementType())
                .amount(request.getAmount())
                .build();
    }

    public MovementResponse toResponse(Movement movement) {
        return MovementResponse.builder()
                .id(movement.getId())
                .date(movement.getDate())
                .movementType(movement.getMovementType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .accountId(movement.getAccount().getId())
                .accountNumber(movement.getAccount().getAccountNumber())
                .build();
    }

    public StatementResponse toStatementResponse(Movement movement) {
        Account account = movement.getAccount();
        return StatementResponse.builder()
                .date(movement.getDate())
                .clientId(account.getClientId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.isStatus())
                .movementType(movement.getMovementType())
                .amount(movement.getAmount())
                .balance(movement.getBalance())
                .build();
    }
}
