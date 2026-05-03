package com.devsu.msfinance.infrastructure.rest.mapper;

import com.devsu.msfinance.domain.model.Account;
import com.devsu.msfinance.infrastructure.rest.dto.AccountRequest;
import com.devsu.msfinance.infrastructure.rest.dto.AccountResponse;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toDomain(AccountRequest request) {
        return Account.builder()
                .accountType(request.getAccountType())
                .initialBalance(request.getInitialBalance())
                .currentBalance(request.getInitialBalance())
                .status(request.getStatus())
                .clientId(request.getClientId())
                .build();
    }

    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .currentBalance(account.getCurrentBalance())
                .status(account.isStatus())
                .clientId(account.getClientId())
                .build();
    }
}
