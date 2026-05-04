package com.devsu.msfinance.infrastructure.persistence;

import com.devsu.msfinance.domain.model.Account;
import com.devsu.msfinance.domain.port.out.AccountRepository;
import com.devsu.msfinance.infrastructure.persistence.jpa.AccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository jpa;

    @Override
    public Account save(Account account) {
        return jpa.save(account);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Account> findAll() {
        return jpa.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }

    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return jpa.existsByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> findByClientId(Long clientId) {
        return jpa.findByClientId(clientId);
    }
}
