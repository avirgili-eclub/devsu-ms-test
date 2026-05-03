package com.devsu.msfinance.domain.port.out;

import com.devsu.msfinance.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(Long id);

    List<Account> findAll();

    void deleteById(Long id);

    boolean existsByAccountNumber(String accountNumber);
}
