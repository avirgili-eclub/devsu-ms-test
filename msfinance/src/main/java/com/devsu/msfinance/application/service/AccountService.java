package com.devsu.msfinance.application.service;

import com.devsu.msfinance.domain.exception.AccountNotFoundException;
import com.devsu.msfinance.domain.model.Account;
import com.devsu.msfinance.domain.port.in.AccountUseCase;
import com.devsu.msfinance.domain.port.out.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements AccountUseCase {

    private final AccountRepository accountRepository;

    @Override
    public Account create(Account account) {
        account.setAccountNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        account.setCurrentBalance(account.getInitialBalance());
        return accountRepository.save(account);
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account update(Long id, Account account) {
        Account existing = getById(id);
        existing.setAccountType(account.getAccountType());
        existing.setStatus(account.isStatus());
        existing.setClientId(account.getClientId());
        return accountRepository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        accountRepository.deleteById(id);
    }
}
